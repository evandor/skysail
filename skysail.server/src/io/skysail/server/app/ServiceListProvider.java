package io.skysail.server.app;

import java.util.Set;

import io.skysail.api.metrics.MetricsCollector;
import io.skysail.api.um.AuthenticationService;
import io.skysail.api.um.AuthorizationService;
import io.skysail.api.validation.ValidatorService;
import io.skysail.server.SkysailComponent;
import io.skysail.server.facets.FacetsProvider;
import io.skysail.server.restlet.filter.FilterParser;
import io.skysail.server.text.TranslationStoreHolder;

@org.osgi.annotation.versioning.ProviderType
public interface ServiceListProvider {

    ValidatorService getValidatorService();

    AuthorizationService getAuthorizationService();
    AuthenticationService getAuthenticationService();

    Set<TranslationRenderServiceHolder> getTranslationRenderServices();
    Set<TranslationStoreHolder> getTranslationStores();

    SkysailComponent getSkysailComponent();

    MetricsCollector getMetricsCollector();

    FacetsProvider getFacetsProvider();

	FilterParser getFilterParser();

}
