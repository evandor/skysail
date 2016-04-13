package io.skysail.server.um.httpdigest;

import java.nio.charset.Charset;
import java.security.Principal;
import java.util.Base64;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.data.ChallengeScheme;
import org.restlet.security.Authenticator;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.security.User;

import io.skysail.api.um.AuthenticationService;

public class HttpDigestAuthenticationService implements AuthenticationService {

	private static final String ANONYMOUS = "anonymous";

	private HttpDigestUserManagementProvider userManagementProvider;

	public HttpDigestAuthenticationService(HttpDigestUserManagementProvider userManagementProvider) {
		this.userManagementProvider = userManagementProvider;
	}

	@Override
	public Authenticator getAuthenticator(String pathTemplate, Context context) {
		ChallengeAuthenticator challengeAuthenticator = new ChallengeAuthenticator(context, ChallengeScheme.HTTP_DIGEST,
				"Skysail Realm");
		challengeAuthenticator.setVerifier(userManagementProvider.getVerifiers().iterator().next());
		return challengeAuthenticator;
	}

	@Override
	public Principal getPrincipal(Request request) {		
		String authorization = request.getHeaders().getFirstValue("Authorization");
		if (authorization != null && authorization.startsWith("Basic")) {
			String base64Credentials = authorization.substring("Basic".length()).trim();
			String credentials = new String(Base64.getDecoder().decode(base64Credentials), Charset.forName("UTF-8"));
			String[] split = credentials.split(":", 2);
			return new User(split[0], split[1]);
		}
		return new User(ANONYMOUS);
	}

	@Override
	public boolean isAuthenticated(Request request) {
		return !getPrincipal(request).getName().equals(ANONYMOUS);
	}

	@Override
	public void updatePassword(User user, String newPassword) {
	}

	@Override
	public void clearCache(String username) {
	}

}
