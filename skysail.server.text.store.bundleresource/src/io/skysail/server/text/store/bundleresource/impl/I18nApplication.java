package io.skysail.server.text.store.bundleresource.impl;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import org.osgi.framework.Bundle;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.event.EventAdmin;

import io.skysail.api.text.Translation;
import io.skysail.api.text.TranslationRenderService;
import io.skysail.api.text.TranslationStore;
import io.skysail.server.ApplicationContextId;
import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.TranslationRenderServiceHolder;
import io.skysail.server.menus.MenuItem;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.restlet.RouteBuilder;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.security.config.SecurityConfigBuilder;
import io.skysail.server.text.TranslationStoreHolder;
import io.skysail.server.utils.TranslationUtils;
import lombok.Getter;

@Component(immediate = true)
public class I18nApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    private List<TranslationStoreHolder> translationStoreHolders = new ArrayList<>();

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    @Getter
    private volatile EventAdmin eventAdmin;

    public I18nApplication() {
        super("i18n");
        addToAppContext(ApplicationContextId.IMG, "/static/img/silk/book_open.png");
    }

    @Override
    protected void defineSecurityConfig(SecurityConfigBuilder securityConfigBuilder) {
        securityConfigBuilder.authorizeRequests().startsWithMatcher("").authenticated();
    }

    @Override
    protected void attach() {
        router.attach(new RouteBuilder("", I18nRootResource.class));
        router.attach(new RouteBuilder("/messages/", MessagesResource.class));
        router.attach(new RouteBuilder("/messages/{key}", MessageResource.class));
        router.attach(new RouteBuilder("/messages/{key}/", PutMessageResource.class));

        router.attach(new RouteBuilder("/messages/{key}/{fieldname}/", PatchMessageResource.class));
    }

    @Override
    public List<MenuItem> getMenuEntries() {
        MenuItem appMenu = new MenuItem("I18N", "/i18n" + getApiVersion().getVersionPath());
        appMenu.setCategory(MenuItem.Category.ADMIN_MENU);
        return Arrays.asList(appMenu);
    }

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MULTIPLE)
    public void addTranslationStoreHolder(TranslationStore service) {
        TranslationStoreHolder holder = new TranslationStoreHolder(service, new HashMap<String, String>());
        this.translationStoreHolders.add(holder);
    }

    public void removeTranslationStoreHolder(TranslationStore service) {
        TranslationStoreHolder holder = new TranslationStoreHolder(service,
                new HashMap<String, String>());
        this.translationStoreHolders.remove(holder);
    }

    public List<BundleMessages> getBundleMessages(Locale locale) {
        Bundle[] bundles = getBundleContext().getBundles();
        List<BundleMessages> result = new ArrayList<>();
        Arrays.stream(bundles).forEach(b -> {
            ClassLoader loader = b.adapt(BundleWiring.class).getClassLoader();
            handleResourceBundle(loader, b, locale, result);
        });
        return result;
    }

    private void handleResourceBundle(ClassLoader loader, Bundle b, Locale locale, List<BundleMessages> result) {
        try {
            ResourceBundle resourceBundle = ResourceBundle.getBundle("translations/messages", locale, loader);
            result.add(new BundleMessages(b, resourceBundle));
        } catch (MissingResourceException mre) {
            // ignore
        }
    }

    /**
     * get a message with the "best" translation (i.e. the first one returned),
     * augmented with the information about which other translation stores are
     * available.
     *
     * @param selectedStore
     */
    public Message getMessage(String key, String selectedStore, SkysailServerResource<?> resource) {
        Set<TranslationStoreHolder> translationStores = serviceListProvider.getTranslationStores();
        Set<TranslationRenderServiceHolder> rendererServices = serviceListProvider.getTranslationRenderServices();
        if (selectedStore != null) {
            Translation translation = TranslationUtils.getTranslation(key, translationStores, selectedStore, resource);
            if (translation != null) {
                return new Message(key, translation, getPreferedRenderer(rendererServices, translation.getValue()));
            }
        }
        List<Translation> translations = TranslationUtils.getAllTranslations(translationStores, key, resource);
        if (translations.size() == 0) {
            Set<String> storeNames = translationStores.stream().map(ts -> {
                return ts.getName();
            }).collect(Collectors.toSet());
            Translation translation = new Translation("", translationStores.iterator().next().getStore().get(),
                    Locale.getDefault());
            return new Message(key, translation, getPreferedRenderer(rendererServices, ""));
        }
        return new Message(key, translations.get(0),
                getPreferedRenderer(rendererServices, translations.get(0).getValue()));
    }

    private TranslationRenderService getPreferedRenderer(Set<TranslationRenderServiceHolder> rendererServices,
            String translationValue) {
        return rendererServices.stream().filter(rs -> {
            return rs.getService().get().applicable(translationValue);
        }).map(TranslationRenderServiceHolder::getService).map(WeakReference::get).findFirst().orElse(null);
    }

    /**
     * tries to create or update a message in a properties file of the matching
     * bundle.
     *
     * The property file has to exist.
     *
     * @param message
     */
    public void setMessage(Message message) {
        translationStoreHolders.stream().forEach(storeHolder -> {
            TranslationRenderService preferredRenderer = message.getPreferredRenderer();
            String text = preferredRenderer != null ? preferredRenderer.addRendererInfo() + message.getMsg()
                    : message.getMsg();
            storeHolder.getStore().get().persist(message.getMsgKey(), text, Locale.getDefault(), getBundleContext());
        });

    }
}
