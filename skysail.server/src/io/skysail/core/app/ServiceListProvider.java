package io.skysail.core.app;

import java.util.Set;

import io.skysail.api.metrics.MetricsCollector;
import io.skysail.api.um.AuthenticationService;
import io.skysail.api.um.AuthorizationService;
import io.skysail.api.validation.ValidatorService;
import io.skysail.core.SkysailComponent;
import io.skysail.core.text.TranslationStoreHolder;
import io.skysail.server.app.TranslationRenderServiceHolder;
import io.skysail.server.facets.FacetsProvider;
import io.skysail.server.filter.FilterParser;

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
	
	SkysailApplicationService getSkysailApplicationService();

}
