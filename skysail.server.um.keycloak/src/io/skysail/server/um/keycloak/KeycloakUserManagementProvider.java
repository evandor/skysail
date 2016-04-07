package io.skysail.server.um.keycloak;

import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;

import io.skysail.api.um.AuthenticationService;
import io.skysail.api.um.AuthorizationService;
import io.skysail.api.um.UserManagementProvider;
import lombok.extern.slf4j.Slf4j;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
@Slf4j
public class KeycloakUserManagementProvider implements UserManagementProvider {

	private KeycloakAuthenticationService authenticationService;
	private KeycloakAuthorizationService authorizationService;

	@Activate
    public void activate(Map<String, String> config) {

//        if (config.get("users") == null) {
//            createDefautConfiguration();
//            return;
//        }
//        cacheManager = new MemoryConstrainedCacheManager();
//        userManagerRepo = new UserManagementRepository(config);
        authenticationService = new KeycloakAuthenticationService(this);
        authorizationService = new KeycloakAuthorizationService(this);
//        SecurityUtils.setSecurityManager(new SkysailWebSecurityManager(authorizationService.getRealm()));
    }

    @Deactivate
    public void deactivate() {
        authenticationService = null;
        authorizationService = null;
//        SecurityUtils.setSecurityManager(null);
//        cacheManager = null;
    }

    
	public AuthenticationService getAuthenticationService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AuthorizationService getAuthorizationService() {
		// TODO Auto-generated method stub
		return null;
	}

}
