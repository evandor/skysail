package io.skysail.server.um.keycloak;

import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;

import io.skysail.api.um.UserManagementProvider;
import lombok.Getter;

/**
 * Central Component for the keycloak user management service. 
 * 
 * Provides both AuthenticationService and AuthorizationService as usual.
 *
 */
@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class KeycloakUserManagementProvider implements UserManagementProvider {

	@Getter
	private KeycloakAuthenticationService authenticationService;
	
	@Getter
	private KeycloakAuthorizationService authorizationService;

	@Activate
    public void activate(Map<String, String> config) {
        authenticationService = new KeycloakAuthenticationService(this);
        authorizationService = new KeycloakAuthorizationService(this);
    }

    @Deactivate
    public void deactivate() {
        authenticationService = null;
        authorizationService = null;
    }

}
