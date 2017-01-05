package io.skysail.server.um.aws;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.ChallengeScheme;

import lombok.extern.slf4j.Slf4j;

/**
 * Overwriting the restlet cookie authenticator to provide a more specific
 * implementation.
 *
 * <p>
 * Optional is set to true to allow anonymous access, the credentials cookie
 * path is set to "/" and a verifier is set.
 * </p>
 *
 */
@Slf4j
public class AwsAuthenticator extends org.restlet.security.ChallengeAuthenticator {

    public AwsAuthenticator(Context context, ChallengeScheme scheme, String realm) {
        super(context, scheme, realm);
//        super(context, realm, encryptSecretKey);
//        this.cacheManager = cacheManager;
//
//        setIdentifierFormName("username");
//        setSecretFormName("password");
//        setLoginFormPath("/v1/_login");
//        setLoginPath("/v1/_login");
//        setLogoutPath(SkysailRootApplication.LOGOUT_PATH);
//        setOptional(true); // we want anonymous users too
//        setVerifier(new SimpleDelegatingVerifier());
    }

    @Override
    protected void afterHandle(Request request, Response response) {
//        ThreadContext.remove();
        super.afterHandle(request, response);
    }

//    @Override
//    protected CookieSetting getCredentialsCookie(Request request, Response response) {
//        CookieSetting credentialsCookie = super.getCredentialsCookie(request, response);
//        credentialsCookie.setPath("/");
//        return credentialsCookie;
//    }

    @Override
    protected boolean authenticate(Request request, Response response) {
        // Restore credentials from the cookie
        //log.debug("getting cookie with name {}", getCookieName());
//        Cookie credentialsCookie = request.getCookies().getFirst(getCookieName());
//        if (credentialsCookie == null || credentialsCookie.getValue() == null) {
//            return super.authenticate(request, response);
//        }
//        String cookieValue = credentialsCookie.getValue();
//        if ("".equals(cookieValue.trim())) {
//            log.warn("provided authentication cookie with value of '{}'", cookieValue);
//            return false;//super.authenticate(request, response);
//        }
//        if (byPassIfPublicUrl(request)) {
//            return false;
//        }
//        request.setChallengeResponse(parseCredentials(cookieValue));
        return super.authenticate(request, response);
    }

//    private boolean byPassIfPublicUrl(Request request) {
//        return "anonymous".equals(request.getOriginalRef().getQueryAsForm().getFirstValue("_asUser"));
//    }
//
//    @Override
//    protected int logout(Request request, Response response) {
//        int result = super.logout(request, response);
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
//        return result;
//    }
}
