package io.skysail.server.um.keycloak.authentication;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Cookie;
import org.restlet.data.CookieSetting;
import org.restlet.ext.crypto.CookieAuthenticator;

import io.skysail.server.app.SkysailRootApplication;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
public class KeycloakOIDCAuthenticator extends CookieAuthenticator {

    private Object skipPattern;

	public KeycloakOIDCAuthenticator(Context context, String realm, byte[] encryptSecretKey) {
        super(context, realm, encryptSecretKey);

        setIdentifierFormName("username");
        setSecretFormName("password");
        setLoginFormPath("/v1/_login");
        setLoginPath("/v1/_login");
        setLogoutPath(SkysailRootApplication.LOGOUT_PATH);
        setOptional(true); // we want anonymous users too
        //setVerifier(new SimpleDelegatingVerifier());
    }
    
    @Override
    protected int beforeHandle(Request request, Response response) {
    	return CONTINUE;//super.beforeHandle(request, response);
    }
    
    @Override
    protected int doHandle(Request request, Response response) {

    	if (shouldSkip(request)) {
        	return super.doHandle(request, response);
        }

    	boolean authenticated = authenticate(request,response);
        if (authenticated) {
//            if (facade.isEnded()) {
//                return;
//            }
//            AuthenticatedActionsHandler actions = new AuthenticatedActionsHandler(deployment, facade);
//            if (actions.handledRequest()) {
//                return;
//            } else {
//                HttpServletRequestWrapper wrapper = tokenStore.buildWrapper();
//                chain.doFilter(wrapper, res);
//                return;
//            }
        }
        challenge(response, true); // stale?
//        AuthChallenge challenge = getChallenge();
//        if (challenge != null) {
//            log.fine("challenge");
//            challenge.challenge(facade);
//            return;
//        }
//        response.sendError(403);    	
        return super.doHandle(request, response);
    }

	@Override
    protected void afterHandle(Request request, Response response) {
       // ThreadContext.remove();
        super.afterHandle(request, response);
    }

    @Override
    protected CookieSetting getCredentialsCookie(Request request, Response response) {
        CookieSetting credentialsCookie = super.getCredentialsCookie(request, response);
        credentialsCookie.setPath("/");
        return credentialsCookie;
    }

    @Override
    protected boolean authenticate(Request request, Response response) {
       
    	return false;
    }

    private boolean byPassIfPublicUrl(Request request) {
        return "anonymous".equals(request.getOriginalRef().getQueryAsForm().getFirstValue("_asUser"));
    }

    @Override
    protected int logout(Request request, Response response) {
        int result = super.logout(request, response);
//        if (cacheManager != null) {
//            Cache<Object, Object> cache = cacheManager.getCache(SkysailHashedCredentialsMatcher.CREDENTIALS_CACHE);
//            // need to find the current value:
//            // cache.remove(value); // NOSONAR
//            // instead: remove all for now
//            cache.clear();
//        }
//        if (Filter.STOP == result) {
//            Subject subject = SecurityUtils.getSubject();
//            subject.logout();
//            response.redirectSeeOther("/");
//        }
        return result;
    }
    
    private boolean shouldSkip(Request request) {
    	//if (skipPattern == null) {
            return false;
//        }
//
//        String requestPath = request.getRequestURI().substring(request.getContextPath().length());
//        return skipPattern.matcher(requestPath).matches();
	}

}
