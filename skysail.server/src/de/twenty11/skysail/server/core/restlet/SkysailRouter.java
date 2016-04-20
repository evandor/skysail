package de.twenty11.skysail.server.core.restlet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.restlet.Restlet;
import org.restlet.ext.raml.RamlSpecificationRestlet;
import org.restlet.resource.Finder;
import org.restlet.resource.ServerResource;
import org.restlet.routing.Filter;
import org.restlet.routing.Router;
import org.restlet.routing.TemplateRoute;
import org.restlet.security.Authenticator;

import com.google.common.base.Predicate;

import io.skysail.domain.Identifiable;
import io.skysail.domain.core.ApplicationModel;
import io.skysail.server.app.ApiVersion;
import io.skysail.server.app.EntityFactory;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.security.config.SecurityConfig;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SkysailRouter extends Router {

    private Map<String, RouteBuilder> pathRouteBuilderMap = new ConcurrentHashMap<String, RouteBuilder>();

    private Predicate<String[]> defaultRolesPredicate;

    private ApiVersion apiVersion;

    private SkysailApplication skysailApplication;

    @Setter
	private SecurityConfig securityConfig;

    public SkysailRouter(SkysailApplication skysailApplication, ApiVersion apiVersion) {
        super(skysailApplication.getContext());
        this.skysailApplication = skysailApplication;
		this.apiVersion = apiVersion;
        
        RamlSpecificationRestlet ramlSpecificationRestlet = skysailApplication.getRamlSpecificationRestlet(getContext());
        skysailApplication.attachRamlDocumentationRestlet(this, "/v1/raml", ramlSpecificationRestlet);
    }

    public TemplateRoute attach(String pathTemplate, Class<? extends ServerResource> targetClass) {
        log.warn("please use a RouteBuilder to attach this resource: {}", targetClass);
        return attach(pathTemplate, createFinder(targetClass));
    }

    public void attach(RouteBuilder routeBuilder) {

        updateApplicationModel(routeBuilder);

        String pathTemplate = routeBuilder.getPathTemplate(apiVersion);
        pathRouteBuilderMap.put(pathTemplate, routeBuilder);
        if (routeBuilder.getTargetClass() == null) {
            attachForTargetClassNull(routeBuilder);
            return;
        }

        if (!routeBuilder.needsAuthentication()) {
            attachForNoAuthenticationNeeded(routeBuilder);
            return;
        }
        Restlet isAuthenticatedAuthorizer = createIsAuthenticatedAuthorizer(pathTemplate, routeBuilder);

        log.info("routing path '{}' -> {}", pathTemplate, routeToString(new StringBuilder(), isAuthenticatedAuthorizer).toString());
        
        attach(pathTemplate, isAuthenticatedAuthorizer);

    }

    private StringBuilder routeToString(StringBuilder sb, Restlet restlet) {
    	sb.append(restlet.getClass().getSimpleName());
    	if (restlet instanceof Filter) {
    		sb.append(" -> ").append(routeToString(new StringBuilder(), ((Filter)restlet).getNext()));
    	} else if (restlet instanceof Finder) {
    		sb.append(" -> ").append(((Finder)restlet).getTargetClass().getSimpleName());
    	} else {
    		log.info("unknown: {}", restlet.getClass().getName());
    	}
    	return sb;
	}

	private void updateApplicationModel(RouteBuilder routeBuilder) {
        ApplicationModel applicationModel = skysailApplication.getApplicationModel();
        if (applicationModel == null) {
        	log.warn("applicationModel is null");
        	return;
        }

        Class<? extends ServerResource> targetClass = routeBuilder.getTargetClass();
        if (targetClass != null) {
            try {
                SkysailServerResource<?> resourceInstance = (SkysailServerResource<?>) targetClass.newInstance();
                Class<? extends Identifiable> parameterizedType = getResourcesGenericType(resourceInstance);
                applicationModel.addOnce(EntityFactory.createFrom(parameterizedType));
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            log.warn("targetClass was null");
        }
    }

    public void detachAll() {
        getRoutes().clear();
    }

    public RouteBuilder getRouteBuilder(String pathTemplate) {
        return pathRouteBuilderMap.get(pathTemplate);
    }

    public Map<String, RouteBuilder> getRouteBuilders() {
        return Collections.unmodifiableMap(pathRouteBuilderMap);
    }

    /**
     * provides, for a given skysail server resource, the path templates the
     * resource was attached to.
     *
     * @param cls
     * @return List of path templates
     */
    public List<String> getTemplatePathForResource(Class<? extends ServerResource> cls) {
        List<String> result = new ArrayList<>();
        for (Entry<String, RouteBuilder> entries : pathRouteBuilderMap.entrySet()) {
            if (entries.getValue() == null || entries.getValue().getTargetClass() == null) {
                continue;
            }
            if (entries.getValue().getTargetClass().equals(cls)) {
                result.add(entries.getKey());
            }
        }
        return result;
    }

    public List<RouteBuilder> getRouteBuildersForResource(Class<?> cls) {
        List<RouteBuilder> result = new ArrayList<>();
        for (Entry<String, RouteBuilder> entry : pathRouteBuilderMap.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            if (entry.getValue().getTargetClass() == null) {
                Restlet restlet = entry.getValue().getRestlet();
                if (restlet == null) {
                    continue;
                }
                handleRestlet(cls, result, entry, restlet);
                continue;
            }
            if (entry.getValue().getTargetClass().equals(cls)) {
                result.add(entry.getValue());
            }
        }
        return result;
    }

    private void handleRestlet(Class<?> cls, List<RouteBuilder> result, Entry<String, RouteBuilder> entries,
            Restlet restlet) {
        if (restlet instanceof Filter) {
            Restlet next = ((Filter) restlet).getNext();
            if (next == null) {
                return;
            }
            if (next.getClass().equals(cls)) {
                result.add(entries.getValue());
                return;
            }
            handleRestlet(cls, result, entries, next);
        } else if (restlet instanceof Finder) {
            Class<? extends ServerResource> targetClass = ((Finder) restlet).getTargetClass();
            if (targetClass == null) {
                return;
            }
            if (targetClass.equals(cls)) {
                result.add(entries.getValue());
                return;
            }

        }
    }

    public Map<String, RouteBuilder> getRoutesMap() {
        return Collections.unmodifiableMap(pathRouteBuilderMap);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (pathRouteBuilderMap != null) {
            for (String key : pathRouteBuilderMap.keySet()) {
                sb.append(key).append(": ").append(pathRouteBuilderMap.get(key)).append("\n");
            }
        }
        return sb.toString();
    }

    public void setAuthorizationDefaults(Predicate<String[]> predicate) {
        this.defaultRolesPredicate = predicate;

    }

    private Restlet createIsAuthenticatedAuthorizer(String pathTemplate, RouteBuilder routeBuilder) {
        Predicate<String[]> predicateToUse = routeBuilder.getRolesForAuthorization() == null ? defaultRolesPredicate
                : routeBuilder.getRolesForAuthorization();
        routeBuilder.authorizeWith(predicateToUse);

        RolesPredicateAuthorizer authorizer = new RolesPredicateAuthorizer(predicateToUse);
        authorizer.setContext(getContext());
        authorizer.setNext(routeBuilder.getTargetClass());
        
        //if (routeBuilder.getAuthenticator() != null) {
        	//Authenticator authenticator = routeBuilder.getAuthenticator();

		// AuthenticatorProvider authenticatorProvider =
		// skysailApplication.getAuthenticator();
		//Authenticator authenticator = skysailApplication.getAuthenticator();// authenticatorProvider.getAuthenticator(skysailApplication.getContext());

//		MapVerifier verifier = new MapVerifier();
//		verifier.getLocalSecrets().put("user", "pass".toCharArray());
//		((ChallengeAuthenticator) authenticator).setVerifier(verifier);
        
        Authenticator authenticationGuard = securityConfig.authenticatorFor(getContext(), pathTemplate);
        		//skysailApplication.getAuthenticationService().getAuthenticator(pathTemplate, getContext());

        authenticationGuard.setNext(authorizer);
		return authenticationGuard;
		// }
		//
		//  
//        Authorizer isAuthenticatedAuthorizer = new AuthenticatedAuthorizer();
//        isAuthenticatedAuthorizer.setNext(authorizer);
//        return isAuthenticatedAuthorizer;
    }

    private void attachForNoAuthenticationNeeded(RouteBuilder routeBuilder) {
        log.info("routing path '{}' -> '{}'", routeBuilder.getPathTemplate(apiVersion), routeBuilder.getTargetClass()
                .getName());
        attach(routeBuilder.getPathTemplate(apiVersion), routeBuilder.getTargetClass());
    }

    private void attachForTargetClassNull(RouteBuilder routeBuilder) {
        Restlet restlet = routeBuilder.getRestlet();
        if (restlet == null) {
            throw new IllegalStateException("RouteBuilder with neither TargetClass nor Restlet defined!");
        }
        log.info("routing path '{}' -> Restlet '{}'", routeBuilder.getPathTemplate(apiVersion), restlet.getClass()
                .getSimpleName());
        restlet.setContext(getContext());
        attach(routeBuilder.getPathTemplate(apiVersion), restlet);
        updateApplicationModel(routeBuilder);

    }

    @SuppressWarnings("unchecked")
    private static Class<? extends Identifiable> getResourcesGenericType(SkysailServerResource<?> resourceInstance) {
        return (Class<? extends Identifiable>) resourceInstance.getParameterizedType();
    }

}
