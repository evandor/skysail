package io.skysail.server.um.auth0.app;

import org.restlet.data.Form;

import io.skysail.server.restlet.resources.EntityServerResource;
import io.skysail.server.um.auth0.Tokens;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Auth0LoginCallbackPage extends EntityServerResource<Credentials> {

	@Override
	public Credentials getEntity() {
		Form query = getQuery();
		System.out.println(query);

		// if (isValidRequest(req)) {
		try {
			Tokens tokens = fetchTokens(query);
			System.out.println(tokens);
			// Auth0User auth0User = auth0Client.getUserProfile(tokens);
			// store(tokens, auth0User, req);
			// NonceUtils.removeNonceFromStorage(req);
			// onSuccess(req, res);
		} catch (RuntimeException ex) {
			log.error(ex.getMessage(),ex);
			// onFailure(req, res, ex);
		}
		// } else {
		// onFailure(req, res, new IllegalStateException("Invalid state or
		// error"));
		// }

		return null;
	}

	private Tokens fetchTokens(Form query) {
		String authorizationCode = query.getFirstValue("code");
		String redirectUri = "http://localhost:2018/Auth0UmApplication/v1/_logincallback";//getRequest().getReferrerRef().toString();// req.getRequestURL().toString();
		Auth0UmApplication app = (Auth0UmApplication) getApplication();
		return app.getTokens(authorizationCode, redirectUri);
	}

}
