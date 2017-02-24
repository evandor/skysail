package io.skysail.api.um;

import java.security.Principal;

import org.osgi.annotation.versioning.ProviderType;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.security.Authenticator;

/**
 * Defines the contract for authentication handling in skysail applications.
 */
@ProviderType
public interface AuthenticationService {

    /**
     * @return a restlet authenticator the application is authenticated against.
     */
    Authenticator getApplicationAuthenticator(Context context, AuthenticationMode authMode);

    /**
     * @param authMode 
     * @return a restlet authenticator the applications' resources are
     *         authenticated against.
     */
    Authenticator getResourceAuthenticator(Context context, AuthenticationMode authMode);

    /**
     * @return whether or not the current request is authenticated.
     */
    boolean isAuthenticated(Request request);

    /**
     * @return the associated principal, i.e. the currently authenticated user.
     */
    Principal getPrincipal(Request request);

    /**
     * skysail can be set up to run with different authentication services, which
     * typically provide a login path to which the generic "/_login" path will
     * be redirected.
     */
    String getLoginPath();

    /**
     * skysail can be set up to run with different authentication services, which
     * typically provide a logout path to which the generic "/_logout" path will
     * be redirected.
     */
    String getLogoutPath();
}
