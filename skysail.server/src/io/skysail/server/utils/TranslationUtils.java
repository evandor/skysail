package io.skysail.server.utils;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import io.skysail.api.text.I18nArgumentsProvider;
import io.skysail.api.text.MessageArguments;
import io.skysail.api.text.Translation;
import io.skysail.server.app.TranslationRenderServiceHolder;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.text.TranslationStoreHolder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TranslationUtils { // NOSONAR

    public static Optional<Translation> getBestTranslation(Set<TranslationStoreHolder> stores, String key,
            SkysailServerResource<?> resource) {
        return getSortedTranslationStores(stores).stream()
                .filter(store -> store.getStore().get() != null)
                .map(store -> createFromStore(key, resource, store, stores))
                .filter(Objects::nonNull)
                .findFirst();
    }

    public static List<Translation> getAllTranslations(Set<TranslationStoreHolder> stores, String key, SkysailServerResource<?> resource) {
        List<TranslationStoreHolder> sortedTranslationStores = getSortedTranslationStores(stores);
        return sortedTranslationStores.stream()
                .filter(store -> store.getStore().get() != null)
                .map(store -> createFromStore(key, resource, store, stores))
                .filter(t -> t != null)
                .collect(Collectors.toList());
    }

    public static Translation getTranslation(String key, Set<TranslationStoreHolder> stores, String selectedStore,
            SkysailServerResource<?> resource) {
        Optional<TranslationStoreHolder> storeToUse = stores.stream()
                .filter(s -> s.getStore().get().getClass().getName().equals(selectedStore))
                .findFirst();
        if (storeToUse.isPresent()) {
            return createFromStore(key, resource, storeToUse.get(), stores);
        }
        return null;
    }

    public static Translation render(Set<TranslationRenderServiceHolder> translationRenderServices, Translation translation) {
        List<TranslationRenderServiceHolder> sortedTranslationRenderServices = getSortedTranslationRenderServices(translationRenderServices);

        return sortedTranslationRenderServices.stream().filter(renderService -> {
            return renderService.getService().get().applicable(translation.getValue());
        }).map(renderService -> {
            String translated = renderService.getService().get().render(translation);
            translation.setValue(translated);
            translation.setRenderer(renderService.getService().get().getClass().getSimpleName());
            return translation;
        }).findFirst().orElse(translation);
    }

    private static List<TranslationRenderServiceHolder> getSortedTranslationRenderServices(
            Set<TranslationRenderServiceHolder> translationRenderServices) {
        List<TranslationRenderServiceHolder> sortedServices = translationRenderServices.stream().sorted((t1, t2) -> {
            return t2.getServiceRanking().compareTo(t1.getServiceRanking());
        }).collect(Collectors.toList());
        return sortedServices;
    }

    private static List<TranslationStoreHolder> getSortedTranslationStores(Set<TranslationStoreHolder> stores) {
        return stores.stream()
            .sorted((t1, t2) -> -t1.getServiceRanking().compareTo(t2.getServiceRanking()))
            .collect(Collectors.toList());
    }

    private static Translation createFromStore(String key, SkysailServerResource<?> resource, TranslationStoreHolder store,
            Set<TranslationStoreHolder> stores) {
        String result = store.getStore().get().get(key, resource.getClass().getClassLoader(), resource.getRequest())
                .orElse(null);
        if (result == null) {
            /*if (key.endsWith(".desc") || key.endsWith(".placeholder") || key.endsWith(".message")) {
                return null;
            } else {
                String[] split = key.split("\\.");
                result = split[split.length-1];
            }*/
        	return null;
        }
        if (resource instanceof I18nArgumentsProvider) {
            MessageArguments messageArguments = ((I18nArgumentsProvider)resource).getMessageArguments();
            return new Translation(
                    result,
                    store.getStore().get(),
                    Locale.getDefault(),
                    messageArguments.get(key));
        }
        return new Translation(
                result,
                store.getStore().get(),
                Locale.getDefault());
    }

}
