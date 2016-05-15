package io.skysail.api.um;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Wraps specific authentication and authorization services.
 *
 */
@ProviderType
public interface UserManagementProvider {

    AuthenticationService getAuthenticationService();

    AuthorizationService getAuthorizationService();

}
