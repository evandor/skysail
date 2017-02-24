package io.skysail.server.um.shiro.authentication;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Cookie;
import org.restlet.data.CookieSetting;
import org.restlet.ext.crypto.CookieAuthenticator;
import org.restlet.routing.Filter;

import io.skysail.server.app.SkysailRootApplication;
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
public class SkysailCookieAuthenticator extends CookieAuthenticator {

    private CacheManager cacheManager;

    public SkysailCookieAuthenticator(Context context, String realm, byte[] encryptSecretKey, boolean optional,
            CacheManager cacheManager) {
        super(context, realm, encryptSecretKey);
        this.cacheManager = cacheManager;

        setIdentifierFormName("username");
        setSecretFormName("password");
        setLoginFormPath("/v1/_login");
        setLoginPath("/v1/_login");
        setLogoutPath(SkysailRootApplication.LOGOUT_PATH);
        // set to false, see https://github.com/evandor/skysail/issues/13
        setOptional(optional); // we want anonymous users too?
        setVerifier(new SimpleDelegatingVerifier());
    }

    @Override
    protected void afterHandle(Request request, Response response) {
        ThreadContext.remove();
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
        // Restore credentials from the cookie
        log.debug("getting cookie with name {}", getCookieName());
        Cookie credentialsCookie = request.getCookies().getFirst(getCookieName());
        if (credentialsCookie == null || credentialsCookie.getValue() == null) {
            return super.authenticate(request, response);
        }
        String cookieValue = credentialsCookie.getValue();
        if ("".equals(cookieValue.trim())) {
            log.warn("provided authentication cookie with value of '{}'", cookieValue);
            return false;//super.authenticate(request, response);
        }
        if (byPassIfPublicUrl(request)) {
            return false;
        }
        request.setChallengeResponse(parseCredentials(cookieValue));
        return super.authenticate(request, response);
    }

    private boolean byPassIfPublicUrl(Request request) {
        return "anonymous".equals(request.getOriginalRef().getQueryAsForm().getFirstValue("_asUser"));
    }

    @Override
    protected int logout(Request request, Response response) {
        int result = super.logout(request, response);
        if (cacheManager != null) {
            Cache<Object, Object> cache = cacheManager.getCache(SkysailHashedCredentialsMatcher.CREDENTIALS_CACHE);
            // need to find the current value:
            // cache.remove(value); // NOSONAR
            // instead: remove all for now
            cache.clear();
        }
        if (Filter.STOP == result) {
            Subject subject = SecurityUtils.getSubject();
            subject.logout();
            response.redirectSeeOther("/");
        }
        return result;
    }
}
