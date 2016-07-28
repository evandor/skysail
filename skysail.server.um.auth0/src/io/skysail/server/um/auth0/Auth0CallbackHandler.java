package io.skysail.server.um.auth0;

public class Auth0CallbackHandler {

	protected String redirectOnSuccess;
	protected String redirectOnFail;
	protected Auth0BundleConfig auth0Config;
	protected Auth0Client auth0Client;

	// @Autowired
	protected void setAuth0Client( Auth0Client auth0Client) {
		this.auth0Client = auth0Client;
	}

	// @Autowired
	protected void setAuth0Config( Auth0BundleConfig auth0Config) {
		this.auth0Config = auth0Config;
		this.redirectOnSuccess = auth0Config.loginRedirectOnSuccess();
		this.redirectOnFail = auth0Config.loginRedirectOnFail();
	}

	/**
	 * Entry point
	 */
	/*public void handle( HttpServletRequest req,  HttpServletResponse res)
			throws IOException, ServletException {
		if (isValidRequest(req)) {
			try {
				 Tokens tokens = fetchTokens(req);
				 Auth0User auth0User = auth0Client.getUserProfile(tokens);
				store(tokens, auth0User, req);
				NonceUtils.removeNonceFromStorage(req);
				onSuccess(req, res);
			} catch (RuntimeException ex) {
				onFailure(req, res, ex);
			}
		} else {
			onFailure(req, res, new IllegalStateException("Invalid state or error"));
		}
	}*/

	/*protected void onSuccess( HttpServletRequest req,  HttpServletResponse res)
			throws ServletException, IOException {
		res.sendRedirect(req.getContextPath() + redirectOnSuccess);
	}

	protected void onFailure( HttpServletRequest req,  HttpServletResponse res,  Exception e)
			throws ServletException, IOException {
		e.printStackTrace();
		 String redirectOnFailLocation = req.getContextPath() + redirectOnFail;
		res.sendRedirect(redirectOnFailLocation);
	}

	protected void store( Tokens tokens,  Auth0User user,  HttpServletRequest req) {
		SessionUtils.setTokens(req, tokens);
		SessionUtils.setAuth0User(req, user);
	}

	protected Tokens fetchTokens( HttpServletRequest req) {
		 String authorizationCode = req.getParameter("code");
		 String redirectUri = req.getRequestURL().toString();
		return auth0Client.getTokens(authorizationCode, redirectUri);
	}

	protected boolean isValidRequest( HttpServletRequest req) throws IOException {
		return !hasError(req) && isValidState(req);
	}

	protected boolean hasError( HttpServletRequest req) {
		return req.getParameter("error") != null;
	}

	protected boolean isValidState( HttpServletRequest req) {
		 String stateFromRequest = req.getParameter("state");
		return NonceUtils.matchesNonceInStorage(req, stateFromRequest);
	}*/
}
