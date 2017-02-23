package io.skysail.api.um;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Wraps specific authentication and authorization services.
 *
 * In a skysail installation, there can be only one UserManagementProvider,
 * providing exactly one authentication and one authorization service.
 *
 */
@ProviderType
public interface UserManagementProvider {

    AuthenticationService getAuthenticationService();

    AuthorizationService getAuthorizationService();
    
    UserManagementRepository getUserManagementRepository();

}
