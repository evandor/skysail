package io.skysail.api.um;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Authentication and Authorization Services
 *
 */
@ProviderType
public interface UserManagementProvider {

    AuthenticationService getAuthenticationService();

    AuthorizationService getAuthorizationService();

}
