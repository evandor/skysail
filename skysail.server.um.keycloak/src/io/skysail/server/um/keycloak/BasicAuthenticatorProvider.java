package io.skysail.server.um.keycloak;

import org.osgi.service.component.annotations.Component;
import org.restlet.Context;
import org.restlet.data.ChallengeScheme;
import org.restlet.security.Authenticator;
import org.restlet.security.ChallengeAuthenticator;

import io.skysail.api.um.AuthenticatorProvider;

@Component
public class BasicAuthenticatorProvider implements AuthenticatorProvider {

	@Override
	public Authenticator getAuthenticator(Context context) {
		return new ChallengeAuthenticator(context, ChallengeScheme.HTTP_BASIC, "My Realm");
	}

}
