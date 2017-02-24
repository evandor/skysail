package io.skysail.api.um;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Wraps specific authentication and authorization services, together with an user management
 * repository which provides the acutal users and their roles.
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
