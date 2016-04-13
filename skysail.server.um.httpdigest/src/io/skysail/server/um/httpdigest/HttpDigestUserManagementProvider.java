package io.skysail.server.um.httpdigest;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.restlet.security.Verifier;

import io.skysail.api.um.UserManagementProvider;
import lombok.Getter;

/**
 * Central Component for the http digest user management service.
 * 
 * Provides both AuthenticationService and AuthorizationService as usual.
 *
 */
@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class HttpDigestUserManagementProvider implements UserManagementProvider {

	@Getter
	private HttpDigestAuthenticationService authenticationService;

	@Getter
	private HttpDigestAuthorizationService authorizationService;

	@Getter
	@Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.AT_LEAST_ONE)
	private volatile Collection<Verifier> verifiers = new HashSet<>();

	@Activate
	public void activate(Map<String, String> config) {
		authenticationService = new HttpDigestAuthenticationService(this);
		authorizationService = new HttpDigestAuthorizationService();
	}

	@Deactivate
	public void deactivate() {
		authenticationService = null;
		authorizationService = null;
	}
	
}
