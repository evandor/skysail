package io.skysail.server.text.store.inmemory;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Component;
import org.restlet.Request;

import io.skysail.api.text.TranslationStore;
import lombok.NonNull;
import lombok.ToString;

/**
 * TODO default strategy (if entry is not found)
 *
 */
@Component(immediate = true,
    property = {
        org.osgi.framework.Constants.SERVICE_RANKING + "=1000",
        "name=InMemoryTranslationStore"
    })
@ToString(of = {})
public class InMemoryTranslationStore implements TranslationStore {

    Map<String, Map<String, String>> translations = new HashMap<>();

    @Override
    public Optional<String> get(String key) {
         return get(key, null, null, Locale.getDefault());
    }

    @Override
    public Optional<String> get(String key, ClassLoader cl) {
        return get(key);
    }

    @Override
    public Optional<String> get(String key, ClassLoader cl, Request request) {
        return get(key);
    }

    @Override
    public Optional<String> get(String key, ClassLoader cl, Request request, Locale locale) {
        Map<String, String> languageMap = translations.get(locale.toString());
        if (languageMap != null) {
            return Optional.ofNullable(languageMap.get(key));
        }
        return Optional.empty();
    }

    @Override
    public boolean persist(@NonNull String key, String message, @NonNull Locale locale, BundleContext bundleContext) {
        String languageKey = locale.toString();
        Map<String, String> languageMap = translations.get(languageKey);
        if (languageMap == null) {
            languageMap = new HashMap<>();
            translations.put(languageKey, languageMap);
        }
        languageMap.put(key, message);
        return true;
    }

}
