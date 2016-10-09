package io.skysail.server.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import org.apache.commons.lang.Validate;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Deactivate;
import org.owasp.html.HtmlPolicyBuilder;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Restlet;
import org.restlet.data.LocalReference;
import org.restlet.data.MediaType;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;
import org.restlet.ext.raml.RamlApplication;
import org.restlet.ext.raml.RamlSpecificationRestlet;
import org.restlet.resource.ServerResource;
import org.restlet.routing.Router;
import org.restlet.security.Authenticator;
import org.restlet.service.CorsService;
import org.restlet.util.RouteList;

import com.google.common.base.Predicate;

import io.skysail.api.text.Translation;
import io.skysail.api.um.AuthenticationService;
import io.skysail.api.um.AuthorizationService;
import io.skysail.api.validation.ValidatorService;
import io.skysail.domain.Identifiable;
import io.skysail.domain.core.Repositories;
import io.skysail.domain.core.repos.Repository;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.HtmlPolicy;
import io.skysail.server.ApplicationContextId;
import io.skysail.server.domain.jvm.JavaApplicationModel;
import io.skysail.server.domain.jvm.JavaEntityModel;
import io.skysail.server.menus.MenuItem;
import io.skysail.server.restlet.RouteBuilder;
import io.skysail.server.restlet.SkysailRouter;
import io.skysail.server.restlet.filter.OriginalRequestFilter;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.security.RolePredicate;
import io.skysail.server.security.config.SecurityConfigBuilder;
import io.skysail.server.services.PerformanceMonitor;
import io.skysail.server.services.PerformanceTimer;
import io.skysail.server.services.ResourceBundleProvider;
import io.skysail.server.text.TranslationStoreHolder;
import io.skysail.server.utils.ClassLoaderDirectory;
import io.skysail.server.utils.CompositeClassLoader;
import io.skysail.server.utils.ReflectionUtils;
import io.skysail.server.utils.TranslationUtils;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * A skysail application is the entry point to provide additional functionality
 * to the skysail server.
 *
 * Typical implementations will overwrite the methods "attach" and "defineSecurityConfig" and be
 * define like this:
 *
 * <pre><code>
 * @Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
 * public class DemoApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {
 * ...
 * }
 * </pre></code>
 *
 * Concurrency note from parent class: instances of this class or its subclasses
 * can be invoked by several threads at the same time and therefore must be
 * thread-safe. You should be especially careful when storing state in member
 * variables.
 *
 */
@Slf4j
public abstract class SkysailApplication extends RamlApplication
        implements ApplicationProvider, ResourceBundleProvider, Comparable<ApplicationProvider> {

    private Map<ApplicationContextId, String> stringContextMap = new HashMap<>(); // NOSONAR

    public static final MediaType SKYSAIL_SERVER_SENT_EVENTS = MediaType.register("text/event-stream",
            "Server Side Events");

    public static final MediaType SKYSAIL_TREE_FORM = MediaType.register("treeform",
            "Html Form as tree representation");

    public static final MediaType SKYSAIL_MAILTO_MEDIATYPE = MediaType.register("mailto", "href mailto target");

    public static final MediaType SKYSAIL_TIMELINE_MEDIATYPE = MediaType.register("timeline",
            "vis.js timeline representation");

    public static final MediaType SKYSAIL_STANDLONE_APP_MEDIATYPE = MediaType.register("standalone",
            "standalone application representation");

    public static final MediaType SKYSAIL_DATA = MediaType.register("data",
            "data representation");

    protected static volatile ServiceListProvider serviceListProvider;

    /**
     * The core domain: a model defining an application with its entities,
     * repositories, entities fields, relations and so on. SkysailApplication
     * itself cannot extend this class as it has to be derived from a restlet
     * application.
     */
    @Getter
    private JavaApplicationModel applicationModel;

    /** the restlet router. */
    protected volatile SkysailRouter router;

    @Getter
    private volatile ComponentContext componentContext;

    @Getter
    private ApiVersion apiVersion = new ApiVersion(1);

    private volatile BundleContext bundleContext;
    private volatile HtmlPolicyBuilder noHtmlPolicyBuilder = new HtmlPolicyBuilder();
    private volatile List<String> parametersToHandle = new CopyOnWriteArrayList<>();
    private volatile Map<String, String> parameterMap = new ConcurrentHashMap<>();
    private volatile List<String> securedByAllRoles = new CopyOnWriteArrayList<>();

    private List<MenuItem> applicationMenu;
    private Map<String, Object> documentedEntities = new ConcurrentHashMap<>();

    public SkysailApplication(String appName) {
        this(appName, new ApiVersion(1));
    }

    public SkysailApplication(String appName, ApiVersion apiVersion) {
        this(appName, apiVersion, Collections.emptyList());
    }

    public SkysailApplication(@NonNull String appName, ApiVersion apiVersion, List<Class<? extends Identifiable>> entityClasses) {
    	setName(appName);
        getEncoderService().getIgnoredMediaTypes().add(SkysailApplication.SKYSAIL_SERVER_SENT_EVENTS);
        getEncoderService().setEnabled(true);
        log.debug("Instanciating new Skysail ApplicationModel '{}'", this.getClass().getSimpleName());
        setName(appName);
        this.apiVersion = apiVersion;
        applicationModel = new JavaApplicationModel(appName);
        entityClasses.forEach(cls -> applicationModel.addOnce(EntityFactory.createFrom(cls, null)));
    }

    /**
     * Overwrite this method to provide routes like this:
     *
     * <pre>
     *   super.attach();
     *   router.attach(new RouteBuilder("/Bookmarks/{id}", BookmarkResource.class));
     * </pre>
     */
    @SuppressWarnings("unchecked")
    protected void attach() {
        if (applicationModel == null) {
            log.warn("no applicationModel defined");
            return;
        }
        if (applicationModel.getEntityIds().isEmpty()) {
            log.warn("there are no entities defined for the applicationModel {}", applicationModel);
            return;
        }
        JavaEntityModel<?> firstClassEntity = (JavaEntityModel<?>) applicationModel
                .getEntity(applicationModel.getEntityIds().iterator().next());
        router.attach(new RouteBuilder("", firstClassEntity.getListResourceClass()));
        router.attach(new RouteBuilder("/", firstClassEntity.getListResourceClass()));

        applicationModel.getEntityIds().stream().map(key -> applicationModel.getEntity(key)) // NOSONAR
                .map(JavaEntityModel.class::cast).forEach(entity -> {
                    if (entity.getListResourceClass() != null) {
                        router.attach(new RouteBuilder("/" + entity.getId(), entity.getListResourceClass()));
                    }
                    if (entity.getPostResourceClass() != null) {
                        router.attach(new RouteBuilder("/" + entity.getId() + "/", entity.getPostResourceClass()));
                    }
                    if (entity.getEntityResourceClass() != null) {
                        router.attach(new RouteBuilder("/" + entity.getId() + "/{id}", entity.getEntityResourceClass()));
                    }
                    if (entity.getPutResourceClass() != null) {
                        router.attach(new RouteBuilder("/" + entity.getId() + "/{id}/", entity.getPutResourceClass()));
                    }
                });
    }

    protected void defineSecurityConfig(SecurityConfigBuilder securityConfigBuilder) {
    }

    /**
     * adding this ClassLoaderDirectory to the router makes the bundle content
     * at "bundleName/bundleVersion" statically available.
     *
     * For example, if you bundle is called "demoapp" (with api version 1) and
     * you add
     *
     * Include-Resource: demoapp/v1=client/dist
     *
     * to your bnd file, the contents of client/dist will be copied to
     * demoapp/v1 inside your bundle jar and are available at
     * http://host:port/demoapp/v1/static/
     */
    protected ClassLoaderDirectory createStaticDirectory() {
        LocalReference localReference = LocalReference.createClapReference(LocalReference.CLAP_THREAD,
                "/" + getName() + "/");

        CompositeClassLoader customCL = new CompositeClassLoader();
        customCL.addClassLoader(Thread.currentThread().getContextClassLoader());
        customCL.addClassLoader(Router.class.getClassLoader());
        customCL.addClassLoader(this.getClass().getClassLoader());

        return new ClassLoaderDirectory(getContext(), localReference, customCL);
    }

    /**
     * Remark: it seems I can use @Activate and @Deactive here (in this parent
     * class), but not @Reference!
     * http://stackoverflow.com/questions/12364484/providing
     * -di-methods-in-abstract-classes
     */
    @Activate
    protected void activate(ComponentContext componentContext) throws ConfigurationException {
        log.debug("Activating ApplicationModel {}", this.getClass().getName());
        this.componentContext = componentContext;
        getApplicationModel().setBundleContext(getBundleContext());
    }

    @Activate
    public void activate(ApplicationConfiguration appConfig, ComponentContext componentContext)
            throws ConfigurationException {
        activate(componentContext);
        if (corsConfigProvided(appConfig)) {
            CorsService corsService = new CorsService();
            configureCorsProperties(appConfig, corsService);
            getServices().add(corsService);
        }
    }

    @Deactivate
    protected void deactivate(ComponentContext componentContext) { // NOSONAR
        log.debug("Deactivating ApplicationModel {}", this.getClass().getName());
        this.componentContext = null;
        this.bundleContext = null;
        if (router != null) {
            router.detachAll();
        }
        log.debug("deactivating UserManagementApplication #" + this.hashCode());
        try {
            getApplication().stop();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        setInboundRoot((Restlet) null);
        setOutboundRoot((Restlet) null);
    }

    public Repository getRepository() {
        log.warn(
                "calling default implementation of getRepository, which should be overwritten if the application provides a repository.");
        return null;
    }

    public Repository getRepository(Class<? extends Identifiable> entityClass) {
        Repository repository = applicationModel.getRepository(entityClass.getName());
        if (repository != null) {
            return repository;
        }
        log.warn("trying to access repository for entity class {}, but failed...", entityClass.getName());
        applicationModel.getRepositoryIds().stream().sorted().forEach(identifier -> { // NOSONAR
            log.info(" - available: {}", identifier);
        });
        return getRepository();
    }

    protected void documentEntities(Object... entitiesToDocument) {
        Arrays.stream(entitiesToDocument).forEach(e -> { // NOSONAR
            documentedEntities.put(e.getClass().getName(), e);
        });
    }

    public static void setServiceListProvider(ServiceListProvider service) {
        serviceListProvider = service;
    }

    protected static void unsetServiceListProvider(ServiceListProvider service) { // NOSONAR
        serviceListProvider = null;
    }

    public Translation translate(String key, String defaultMsg, SkysailServerResource<?> resource) {

        Set<TranslationStoreHolder> translationStores = serviceListProvider.getTranslationStores();
        Optional<Translation> bestTranslationFromAStore = TranslationUtils.getBestTranslation(translationStores, key,
                resource);
        if (!bestTranslationFromAStore.isPresent()) {
            return new Translation(defaultMsg, null, Collections.emptySet());
        }
        Set<TranslationRenderServiceHolder> translationRenderServices = serviceListProvider
                .getTranslationRenderServices();
        return TranslationUtils.render(translationRenderServices, bestTranslationFromAStore.get());
    }

    /**
     * @return the bundle context.
     */
    public BundleContext getBundleContext() {
        if (this.bundleContext != null) {
            return bundleContext;
        }
        return componentContext != null ? componentContext.getBundleContext() : null;
    }

    @Override
    public synchronized Restlet createInboundRoot() {
        super.createInboundRoot();
        log.info("creating new Router in {}", this.getClass().getName());
        router = new SkysailRouter(this, apiVersion);

        log.info("adding extensions to metadata service");
        getMetadataService().addExtension("eventstream", SKYSAIL_SERVER_SENT_EVENTS);
        getMetadataService().addExtension("treeform", SKYSAIL_TREE_FORM);
        getMetadataService().addExtension("mailto", SKYSAIL_MAILTO_MEDIATYPE);
        getMetadataService().addExtension("timeline", SKYSAIL_TIMELINE_MEDIATYPE);
        getMetadataService().addExtension("standalone", SKYSAIL_STANDLONE_APP_MEDIATYPE);
        getMetadataService().addExtension("data", SKYSAIL_DATA);

        getMetadataService().addExtension("x-www-form-urlencoded", MediaType.APPLICATION_WWW_FORM);

        // see
        // http://nexnet.wordpress.com/2010/09/29/clap-protocol-in-restlet-and-osgi/
        log.info("adding protocols");
        getConnectorService().getClientProtocols().add(Protocol.HTTP);
        getConnectorService().getClientProtocols().add(Protocol.FILE);
        getConnectorService().getClientProtocols().add(Protocol.CLAP);

        SecurityConfigBuilder securityConfigBuilder = new SecurityConfigBuilder(getApiVersion());
        defineSecurityConfig(securityConfigBuilder);
        securityConfigBuilder.setAuthenticationService(serviceListProvider.getAuthenticationService());
        router.setSecurityConfig(securityConfigBuilder.build());

        getContext().setDefaultEnroler(serviceListProvider.getAuthorizationService().getEnroler());

        log.debug("attaching application-specific routes");

        attach();

        log.debug("creating original request filter...");
        OriginalRequestFilter originalRequestFilter = new OriginalRequestFilter(getContext());
        originalRequestFilter.setNext(router);

        AuthenticationService authenticationService = getAuthenticationService();
        Authenticator authenticationGuard = authenticationService.getApplicationAuthenticator(getContext());

        authenticationGuard.setNext(originalRequestFilter);
        return authenticationGuard;
    }

    @Override
    public RamlSpecificationRestlet getRamlSpecificationRestlet(Context context) {
        return new SkysailRamlSpecificationRestlet(context, this);
    }

    public void attachToRouter(String key, Class<? extends ServerResource> executor) {
        router.attach(key, executor);
    }

    public void attachToRouter(String key, Restlet restlet) {
        router.attach(key, restlet);
    }

    public void detachFromRouter(Class<?> executor) {
        router.detach(executor);
    }

    public RouteList getRoutes() {
        return router.getRoutes();
    }

    public Map<String, RouteBuilder> getRoutesMap() {
        return router.getRoutesMap();
    }

    public Map<String, RouteBuilder> getSkysailRoutes() {
        if (router == null) {
            log.error("router of application '{}' is null! - access the skysail server at least once with your browser",
                    this.getName());
            return Collections.emptyMap();
        }
        return router.getRouteBuilders();
    }

    public List<RouteBuilder> getRouteBuildersForResource(Class<? extends ServerResource> cls) {
        return router.getRouteBuildersForResource(cls);
    }

    @Override
    public SkysailApplication getApplication() {
        return this;
    }

    @Override
    public <T extends SkysailServerResource<?>> List<String> getTemplatePaths(Class<T> cls) {
        List<String> paths = router.getTemplatePathForResource(cls);
        List<String> result = new ArrayList<>();
        for (String path : paths) {
            result.add("/" + getName() + path);
        }
        return result;
    }

    /**
     * get the route builders.
     *
     * @param cls
     * @return list of route builders
     */
    public <T extends SkysailServerResource<?>> List<RouteBuilder> getRouteBuilders(Class<T> cls) {
        if (router == null) {
            return Collections.emptyList();
        }
        return router.getRouteBuildersForResource(cls);
    }

    @Override
    public List<ResourceBundle> getResourceBundles() {
        List<ResourceBundle> result = new ArrayList<>();
        addResourceBundleIfExistent(result, "en", this.getClass().getClassLoader());
        addResourceBundleIfExistent(result, "de", this.getClass().getClassLoader());
        return result;
    }

    private void addResourceBundleIfExistent(List<ResourceBundle> result, String language, ClassLoader classLoader) {
        try {
            ResourceBundle resourceBundleEn = ResourceBundle.getBundle("translations/messages", new Locale(language),
                    classLoader);
            if (resourceBundleEn != null) {
                log.info("found resource bundle for language '{}', classloader {}:", language, classLoader.toString());
                Enumeration<String> keys = resourceBundleEn.getKeys();
                while (keys.hasMoreElements()) {
                    String nextElement = keys.nextElement();
                    log.info(" {} -> {}", nextElement, resourceBundleEn.getString(nextElement));
                }
                result.add(resourceBundleEn);
            }
        } catch (MissingResourceException mre) { // NOSONAR
            // ok
        }
    }

    public String getLinkTo(Reference reference, Class<? extends ServerResource> cls) {
        List<String> relativePaths = router.getTemplatePathForResource(cls);
        return reference.toString() + relativePaths.get(0);
    }

    public void setComponentContext(ComponentContext componentContext) {
        this.componentContext = componentContext;
    }

    /**
     * Some bundles set the componentContext, others (via blueprint) only the
     * bundleContext... need to revisit
     *
     * @return
     */
    public Bundle getBundle() {
        if (this.bundleContext != null) {
            return this.bundleContext.getBundle();
        }
        if (componentContext == null) {
            return null;
        }
        return componentContext.getBundleContext().getBundle();
    }

    public AuthenticationService getAuthenticationService() {
        return serviceListProvider.getAuthenticationService();
    }

    public AuthorizationService getAuthorizationService() {
        return serviceListProvider.getAuthorizationService();
    }

    public void handleParameters(List<String> parametersToHandle) {
        this.parametersToHandle = parametersToHandle;
    }

    public List<String> getParametersToHandle() {
        return parametersToHandle;
    }

    public void addRequestParameter(String paramName, String value) {
        parameterMap.put(paramName, value);
    }

    public HtmlPolicyBuilder getHtmlPolicy(Class<?> entityClass, String fieldName) {
        HtmlPolicyBuilder result = noHtmlPolicyBuilder;
        List<java.lang.reflect.Field> fields = ReflectionUtils.getInheritedFields(entityClass);
        for (java.lang.reflect.Field field : fields) {
            Optional<Field> formField = noMatch(field, fieldName);
            if (!formField.isPresent()) {
                continue;
            }
            HtmlPolicy htmlPolicy = formField.get().htmlPolicy();
            List<String> allowedElements = htmlPolicy.getAllowedElements();
            HtmlPolicyBuilder htmlPolicyBuilder = new HtmlPolicyBuilder();
            htmlPolicyBuilder.allowElements(allowedElements.toArray(new String[allowedElements.size()]));
            return htmlPolicyBuilder;
        }

        return result;
    }

    /**
     * get the encryption parameter.
     *
     * @param entityClass
     * @param fieldName
     * @return
     */
    public String getEncryptionParameter(Class<?> entityClass, String fieldName) {
        List<java.lang.reflect.Field> fields = ReflectionUtils.getInheritedFields(entityClass);
        for (java.lang.reflect.Field field : fields) {
            Optional<Field> formField = noMatch(field, fieldName);
            if (!formField.isPresent()) {
                continue;
            }
            return formField.get().encryptWith();
        }

        return null;
    }

    private Optional<Field> noMatch(java.lang.reflect.Field field, String fieldName) {
        if (!field.getName().equals(fieldName)) {
            return Optional.ofNullable(null);
        }
        Field formField = field.getAnnotation(Field.class);
        if (formField == null) {
            return Optional.ofNullable(null);
        }
        return Optional.of(formField);
    }

    protected void setSecuredByRoles(String... rolenames) {
        Validate.noNullElements(rolenames);
        this.securedByAllRoles = Arrays.asList(rolenames);
    }

    public List<String> getSecuredByAllRoles() {
        return Collections.unmodifiableList(securedByAllRoles);
    }

    @Override
    public int compareTo(ApplicationProvider o) {
        return this.getApplication().getName().compareTo(o.getApplication().getName());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName()).append(" (SkysailApplication)\n");
        sb.append("Router: ").append(router).append("\n");
        return sb.toString();
    }

    /**
     * xxx.
     *
     * @param roles
     * @return
     */
    public static Predicate<String[]> anyOf(String... roles) {
        List<RolePredicate> predicates = Arrays.stream(roles).map(r -> new RolePredicate(r)) // NOSONAR
                .collect(Collectors.toList());
        return com.google.common.base.Predicates.or(predicates);
    }

    /**
     * yyy.
     *
     * @param roles
     * @return
     */
    public static Predicate<String[]> allOf(String... roles) {
        List<RolePredicate> predicates = Arrays.stream(roles).map(r -> new RolePredicate(r)) // NOSONAR
                .collect(Collectors.toList());
        return com.google.common.base.Predicates.and(predicates);
    }

    public Collection<PerformanceMonitor> getPerformanceMonitors() {
        Collection<PerformanceMonitor> performanceMonitors = serviceListProvider.getPerformanceMonitors();
        return Collections.unmodifiableCollection(performanceMonitors);
    }

    protected void addToAppContext(ApplicationContextId id, String value) {
        stringContextMap.put(id, value);
    }

    public String getFromContext(ApplicationContextId id) {
        return stringContextMap.get(id);
    }

    public ValidatorService getValidatorService() {
        return serviceListProvider.getValidatorService();
    }

    public Set<PerformanceTimer> startPerformanceMonitoring(String identifier) {
        Collection<PerformanceMonitor> performanceMonitors = serviceListProvider.getPerformanceMonitors();
        return performanceMonitors.stream().map(monitor -> monitor.start(identifier)).collect(Collectors.toSet());
    }

    public void stopPerformanceMonitoring(Set<PerformanceTimer> perfTimer) {
        perfTimer.stream().forEach(timer -> timer.stop());
    }

    public List<MenuItem> getMenuEntriesWithCache() {
        if (applicationMenu == null) {
            applicationMenu = createMenuEntries();
        }
        return applicationMenu;
    }

    public List<MenuItem> createMenuEntries() {
        return Collections.emptyList();
    }

    public void invalidateMenuCache() {
        applicationMenu = null;
    }

    public void setRepositories(Repositories repos) {
        applicationModel.setRepositories(repos);
    }

    public List<MenuItem> getMenuEntries() {
        MenuItem appMenu = new MenuItem(getName(), "/" + getName() + getApiVersion().getVersionPath());
        appMenu.setCategory(MenuItem.Category.APPLICATION_MAIN_MENU);
        return Arrays.asList(appMenu);
    }

    protected void addService(Object service) {
        getContext().getAttributes().put(service.getClass().getName(), service);
    }

    private boolean corsConfigProvided(ApplicationConfiguration appConfig) {
        return appConfig.corsOrigins() != null && appConfig.corsOrigins().length > 0;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void configureCorsProperties(ApplicationConfiguration appConfig, CorsService corsService) {
        HashSet allowedOrigins = new HashSet(Arrays.asList(appConfig.corsOrigins()));
        log.info("setting CORS allowed Origins for application {} to {}.", getName(), allowedOrigins);
        corsService.setAllowedOrigins(allowedOrigins);
        if (Boolean.valueOf(appConfig.corsAllowCredentials())) {
            log.info("setting CORS allowed Credentials for application {} to 'true'.", getName());
            corsService.setAllowedCredentials(true);
        }
        if (appConfig.corsAllowedHeaders() != null) {
            HashSet allowedHeaders = new HashSet(Arrays.asList(appConfig.corsAllowedHeaders()));
            log.info("setting CORS allowed Headers for application {} to {}.", getName(), allowedHeaders);
            corsService.setAllowedHeaders(allowedHeaders);
        }
        if (appConfig.corsExposedHeaders() != null) {
            HashSet exposedHeaders = new HashSet(Arrays.asList(appConfig.corsExposedHeaders()));
            log.info("setting CORS exposed Headers for application {} to {}.", getName(), exposedHeaders);
            corsService.setExposedHeaders(exposedHeaders);
        }
    }

    public boolean isAuthenticated(Request request) {
        if (serviceListProvider == null || serviceListProvider.getAuthenticationService() == null) {
            log.warn(
                    "serviceListProvider or AuthenticationService is null, returning isAuthenticated => false by default.");
            return false;
        }
        return serviceListProvider.getAuthenticationService().isAuthenticated(request);
    }
}
