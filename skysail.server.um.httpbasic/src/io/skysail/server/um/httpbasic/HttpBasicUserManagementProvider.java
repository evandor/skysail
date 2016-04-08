package io.skysail.server.um.httpbasic;

import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;

import io.skysail.api.um.UserManagementProvider;
import lombok.Getter;

/**
 * Central Component for the httpbasic user management service. 
 * 
 * Provides both AuthenticationService and AuthorizationService as usual.
 *
 */
@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class HttpBasicUserManagementProvider implements UserManagementProvider {

	@Getter
	private HttpBasicAuthenticationService authenticationService;
	
	@Getter
	private HttpBasicAuthorizationService authorizationService;

	@Activate
    public void activate(Map<String, String> config) {
        authenticationService = new HttpBasicAuthenticationService(this);
        authorizationService = new HttpBasicAuthorizationService(this);
    }

    @Deactivate
    public void deactivate() {
        authenticationService = null;
        authorizationService = null;
    }

}
