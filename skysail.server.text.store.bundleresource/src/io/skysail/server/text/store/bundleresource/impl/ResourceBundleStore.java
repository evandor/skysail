package io.skysail.server.text.store.bundleresource.impl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.event.EventAdmin;
import org.restlet.Request;
import org.restlet.util.Series;

import io.skysail.api.text.TranslationStore;
import io.skysail.server.utils.HeadersUtils;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Component(immediate = true, property = { org.osgi.framework.Constants.SERVICE_RANKING + "=100" })
@Slf4j
@ToString(of = {})
// http://viralpatel.net/blogs/eclipse-resource-is-out-of-sync-with-the-filesystem/
public class ResourceBundleStore implements TranslationStore {

    private static final String TRANSLATIONS_MESSAGES_PATH = "translations/messages";

    private static final int MIN_MATCH_LENGTH = 20;
    
    private ComponentContext ctx;

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    @Getter
    private volatile EventAdmin eventAdmin;

    @Activate
    private void activate(ComponentContext ctx) {
        this.ctx = ctx;
    }

    @Deactivate
    private void deactivate(ComponentContext ctx) {
        this.ctx = null;
    }

    /**
     * get the ResourceBundle translation for the given key using the default
     * locale.
     */
    @Override
    public Optional<String> get(String key) {
        return Optional.ofNullable(translate(key,
                ResourceBundle.getBundle(TRANSLATIONS_MESSAGES_PATH, Locale.getDefault())));
    }

    /**
     * get the ResourceBundle translation for the given key and classloader
     * using the default locale.
     */
    @Override
    public Optional<String> get(String key, ClassLoader cl) {
        return Optional.ofNullable(translate(key,
                ResourceBundle.getBundle(TRANSLATIONS_MESSAGES_PATH, Locale.getDefault(), cl)));
    }

    /**
     * get the ResourceBundle translation for the given key using the first
     * match for the accepted locales according to the request.
     */
    @Override
    public Optional<String> get(String key, ClassLoader cl, Request request) {
        Optional<String> translation = findAcceptedLanguages(request).stream()
            .map(l -> translate(key, getBundle(cl, l)))
            .filter(Objects::nonNull)
            .findFirst();

        if (!translation.isPresent()) {
            Optional<BundleMessages> bundleMessage = getBundleMessages(new Locale("en")).stream()
                .filter(bm -> bm.getMessages().keySet().contains(key))
                .findFirst();
            
            if (bundleMessage.isPresent()) {
                return Optional.of(bundleMessage.get().getMessages().get(key));
            }
        }
        return translation;
    }

    /**
     * get the ResourceBundle translation for the given key using the provided
     * locale.
     */
    @Override
    public Optional<String> get(String key, ClassLoader cl, Request request, Locale locale) {
        return findAcceptedLanguages(request).stream().filter(l -> {
            return translate(key, ResourceBundle.getBundle(TRANSLATIONS_MESSAGES_PATH, locale, cl)) != null;
        }).findFirst();
    }
    
    @Override
    public boolean persist(String key, String message, Locale locale, BundleContext bundleContext) {
        List<BundleMessages> messages = getBundleMessages(locale, bundleContext);
        Optional<BundleMessages> bundleMessage = messages.stream().filter(bm -> {
            return bm.getMessages().keySet().contains(key);
        }).findFirst();
        String updatePath;
        if (bundleMessage.isPresent()) {
            updatePath = update(key, message, bundleMessage.get());
        } else {
            updatePath = create(key, message, bundleContext);
        }
        return updatePath != null;
    }

    private List<BundleMessages> getBundleMessages(Locale locale) {
        if (ctx == null) {
            return Collections.emptyList();
        }
        Bundle[] bundles = ctx.getBundleContext().getBundles();
        List<BundleMessages> result = new ArrayList<>();
        Arrays.stream(bundles).forEach(b -> {
            BundleWiring wiring = b.adapt(BundleWiring.class);
            if (wiring != null) {
                ClassLoader loader = wiring.getClassLoader();
                handleResourceBundle(loader, b, locale, result);
            }
        });
        return result;
    }

    private ResourceBundle getBundle(ClassLoader cl, String l) {
        try {
            return ResourceBundle.getBundle(TRANSLATIONS_MESSAGES_PATH, new Locale(l), cl);
        } catch (MissingResourceException e) {
            return null;
        }
    }

    private List<String> findAcceptedLanguages(Request request) {
        Series<?> headers = (Series<?>) request.getAttributes().get("org.restlet.http.headers");
        if (headers == null) {
            return Collections.emptyList();
        }
        return HeadersUtils.parseAcceptedLanguages(headers.getFirstValue("Accept-language"));
    }

    private String translate(String key, ResourceBundle resourceBundle) {
        if (resourceBundle == null) {
            return null;
        }
        try {
            return resourceBundle.getString(key);
        } catch (MissingResourceException mre) {
            log.debug(mre.getMessage());
        }
        return null;
    }

    private List<BundleMessages> getBundleMessages(Locale locale, BundleContext bundleContext) {
        Bundle[] bundles = bundleContext.getBundles();
        List<BundleMessages> result = new ArrayList<>();
        Arrays.stream(bundles).filter(b -> {
            return b.getState() == Bundle.ACTIVE;
        }).forEach(b -> {
            ClassLoader loader = b.adapt(BundleWiring.class).getClassLoader();
            handleResourceBundle(loader, b, locale, result);
        });
        return result;
    }

    private String create(String key, String message, BundleContext bundleConetxt) {
        List<BundleMessages> messages = getBundleMessages(new Locale("en"), bundleConetxt);
        int lastIndexOfUppercaseLetter = firstIndexOfUppercaseLetter(key);
        if (lastIndexOfUppercaseLetter < MIN_MATCH_LENGTH) {
            return null;
        }
        String match = findMatch(key);
        if (StringUtils.isEmpty(match)) {
            log.warn("could not create new translation, as there was not match for the key '{}'", key);
            return null;
        }
        for (BundleMessages bundleMessages : messages) {
            Map<String, String> msgs = bundleMessages.getMessages();
            Optional<String> found = msgs.keySet().stream().filter(k -> {
                return k.startsWith(match);
            }).findFirst();
            if (found.isPresent()) {
                return update(key, message, bundleMessages);
            }
        }
        return null;
    }

    private String findMatch(String msgKey) {
        int firstIndexOfUppercaseLetter = firstIndexOfUppercaseLetter(msgKey);
        return msgKey.substring(0, firstIndexOfUppercaseLetter - 1);
    }

    private String update(String key, String message, BundleMessages bundleMessages) {
        String propertyFileName = bundleMessages.getBaseBundleName();
        Bundle bundle = bundleMessages.getBundle();
        Path propertiesFile = Paths.get("..", bundle.getSymbolicName().replace(".core", ""), "resources",
                propertyFileName + ".properties");

        PropertiesConfiguration props;
        try {
            props = new PropertiesConfiguration(propertiesFile.toFile());
            props.setProperty(key, message);
            props.save();
        } catch (ConfigurationException e) {
            log.error(e.getMessage(), e);
            return null;
        }
        return propertiesFile.toString();
    }

    private int firstIndexOfUppercaseLetter(String str) {
        for (int i = 0; i < str.length() - 1; i++) {
            if (Character.isUpperCase(str.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

    private void handleResourceBundle(ClassLoader loader, Bundle b, Locale locale, List<BundleMessages> result) {
        try {
            ResourceBundle resourceBundle = ResourceBundle.getBundle(TRANSLATIONS_MESSAGES_PATH, locale, loader);
            result.add(new BundleMessages(b, resourceBundle));
        } catch (MissingResourceException mre) { // NOSONAR
            // ignore
        }
    }


}
