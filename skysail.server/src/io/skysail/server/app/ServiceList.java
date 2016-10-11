package io.skysail.server.app;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.restlet.Context;

import io.skysail.api.metrics.MetricsCollector;
import io.skysail.api.text.TranslationRenderService;
import io.skysail.api.text.TranslationStore;
import io.skysail.api.um.AuthenticationService;
import io.skysail.api.um.AuthorizationService;
import io.skysail.api.um.UserManagementProvider;
import io.skysail.api.validation.ValidatorService;
import io.skysail.server.SkysailComponent;
import io.skysail.server.text.TranslationStoreHolder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * manages the list of default services which will be injected into all
 * (currently available) skysail applications (by calling the
 * applicationsListProvider).
 *
 * <p>
 * This class connected with the {@link ApplicationList}, which keeps track of
 * all currently available skysail applications and injects the services once a
 * new application becomes available.
 * </p>
 *
 */
@Component(immediate = false)
@Slf4j
public class ServiceList implements ServiceListProvider {

    @Getter
    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    public volatile ValidatorService validatorService;

    @Getter
    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    private volatile ApplicationListProvider applicationListProvider;

    private volatile AuthorizationService authorizationService;
    private volatile AuthenticationService authenticationService;

    @Getter
    private volatile Set<TranslationRenderServiceHolder> translationRenderServices = Collections
            .synchronizedSet(new HashSet<>());

    @Getter
    private volatile Set<TranslationStoreHolder> translationStores = Collections.synchronizedSet(new HashSet<>());

    //@Reference(cardinality = ReferenceCardinality.OPTIONAL)
    private SkysailComponentProvider skysailComponentProvider;
    
//    @Getter
//    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MULTIPLE)
//    public volatile Collection<MetricsCo> performanceMonitors = new HashSet<>();
    
    @Getter
    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    private MetricsCollector metricsCollector;
    
    @Activate
    public void activate() {
        applicationListProvider.attach(skysailComponentProvider.getSkysailComponent());
    }

    @Deactivate
    public void deactivate() {
    }
    
    /** === UserManagementProvider Service ============================== */

    @Reference(policy = ReferencePolicy.STATIC, cardinality = ReferenceCardinality.MANDATORY)
    public synchronized void setUserManagementProvider(UserManagementProvider provider) {
    	log.info("USER MANAGEMENT PROVIDER: setting provider to '{}'", provider.getClass().getName());
        this.authenticationService = provider.getAuthenticationService();
        this.authorizationService = provider.getAuthorizationService();
    }

    public synchronized void unsetUserManagementProvider(UserManagementProvider provider) {
    	log.info("USER MANAGEMENT PROVIDER: unsetting provider '{}'", provider.getClass().getName());
        this.authenticationService = null;
        this.authorizationService = null;
    }

    @Override
    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    @Override
    public AuthorizationService getAuthorizationService() {
        return authorizationService;
    }

    /** === ApplicationListProvider Service ============================== */


    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL)
    public synchronized void setSkysailComponentProvider(SkysailComponentProvider service) {
        skysailComponentProvider = service;
        Context appContext = skysailComponentProvider.getSkysailComponent().getContext().createChildContext();
        getSkysailApps().forEach(app -> app.setContext(appContext));
    }

    public synchronized void unsetSkysailComponentProvider(SkysailComponentProvider service) { // NOSONAR
        this.skysailComponentProvider = null;
        getSkysailApps().forEach(a -> a.setContext(null));
    }

    @Override
    public SkysailComponent getSkysailComponent() {
        return skysailComponentProvider.getSkysailComponent();
    }

    /** === TranslationRenderService ============================== */

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MULTIPLE)
    public synchronized void addTranslationRenderService(TranslationRenderService service, Map<String, String> props) {
        TranslationRenderServiceHolder holder = new TranslationRenderServiceHolder(service, props);
        this.translationRenderServices.add(holder);
    }

    public synchronized void removeTranslationRenderService(TranslationRenderService service) {
        TranslationRenderServiceHolder holder = new TranslationRenderServiceHolder(service,
                new HashMap<String, String>());
        this.translationRenderServices.remove(holder);
    }

    /** === TranslationStoresService ============================== */

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MULTIPLE)
    public synchronized void addTranslationStore(TranslationStore service, Map<String, String> props) {
        TranslationStoreHolder holder = new TranslationStoreHolder(service, props);
        this.translationStores.add(holder);
    }

    public synchronized void removeTranslationStore(TranslationStore service) {
        TranslationStoreHolder holder = new TranslationStoreHolder(service, new HashMap<String, String>());
        this.translationStores.remove(holder);
    }


    private Stream<SkysailApplication> getSkysailApps() {
        if (applicationListProvider == null) {
            return Stream.empty();
        }
        return applicationListProvider.getApplications().stream();
    }

}
