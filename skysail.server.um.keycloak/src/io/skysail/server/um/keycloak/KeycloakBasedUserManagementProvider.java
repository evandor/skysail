package io.skysail.server.um.keycloak;

import java.util.Optional;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.restlet.security.User;

import io.skysail.api.um.UserManagementProvider;
import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.um.domain.SkysailUser;
import io.skysail.server.um.keycloak.authentication.KeycloakAuthenticationService;
import io.skysail.server.um.keycloak.authorization.KeycloakAuthorizationService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * A UserManagerProvider for the shiro bundle.
 *
 */
@Component(
	immediate = true,
	property = { "service.ranking:Integer=500" })
@Slf4j
public class KeycloakBasedUserManagementProvider implements UserManagementProvider {

    @Getter
	@Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY, target = "(name=KeycloakUmApplication)")
	private volatile ApplicationProvider skysailApplication;

    @Getter
    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY)
    private volatile io.skysail.api.um.UserManagementRepository userManagementRepository;

    @Getter
	private KeycloakAuthorizationService authorizationService;

    @Getter
	private KeycloakAuthenticationService authenticationService;

    @Activate
    public void activate() {
    	log.info("USER MANAGEMENT PROVIDER: activating provider '{}'", this.getClass().getName());
//        cacheManager = new MemoryConstrainedCacheManager();
        authenticationService = new KeycloakAuthenticationService(this);
        authorizationService = new KeycloakAuthorizationService(this);
//        SecurityUtils.setSecurityManager(new SkysailWebSecurityManager(authorizationService.getRealm()));
    }

    @Deactivate
    public void deactivate() {
    	log.info("USER MANAGEMENT PROVIDER: deactivating provider '{}'", this.getClass().getName());
//        authenticationService = null;
//        authorizationService = null;
//        SecurityUtils.setSecurityManager(null);
//        cacheManager = null;
    }

    public SkysailUser getByUsername(String username) {
        Optional<User> user = userManagementRepository.getUser(username);
        if (user.isPresent()) {
        	return new SkysailUser(user.get().getIdentifier(), new String(user.get().getSecret()), user.get().getIdentifier());
        } else {
        	log.info("user {} was not found in the user repository", username);
        	return null;
        }
    }

    public SkysailUser getByPrincipal(String username) {
    	return getByUsername(username);
    }

}
