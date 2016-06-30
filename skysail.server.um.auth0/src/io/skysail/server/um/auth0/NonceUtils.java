package io.skysail.server.um.auth0;

import java.util.concurrent.ConcurrentMap;

import javax.servlet.http.HttpServletRequest;

/**
 * Convenience Utils methods for manipulating the nonce key/value pair held in state param
 * Used for CSRF protection - should always be sent with login request
 * <p>
 * We assign on login, and remove on successful callback completion
 * callback request is checked for validity by correctly matching state in http request
 * with state held in storage (library uses http session)
 * <p>
 * By using a nonce attribute in the state request param, we can also add additional attributes
 * as needed such as externalRedirectURL for SSO scenarios etc
 * <p>
 * Examples of query param:
 * <p>
 * queryString = "nonce=B4AD596E418F7CE02A703B42F60BAD8F";
 * queryString = "externalRedirectUrl=http://localhost:3099/callback";
 * queryString = "nonce=B4AD596E418F7CE02A703B42F60BAD8F&externalRedirectUrl=http://localhost:3099/callback";
 */
public class NonceUtils {

    public static final String NONCE_KEY = "nonce";


    public static void addNonceToStorage(ConcurrentMap<String, Object> concurrentMap, String string) {
        String stateFromStorage = (String)concurrentMap.get(SessionUtils.STATE);//SessionUtils.getState(concurrentMap) != null ? SessionUtils.getState(concurrentMap) : "";
        // only add if no existing entry..
        //if (!keyInQueryParams(stateFromStorage, NONCE_KEY)) {
            final String updatedState = NonceFactory.create();//addOrReplaceInQueryParams(stateFromStorage, NONCE_KEY, NonceFactory.create());
            //SessionUtils.setState(req, updatedState);
            concurrentMap.put(SessionUtils.STATE, updatedState);
        //}
    }

    private static boolean keyInQueryParams(String stateFromStorage, String nonceKey) {
    	
		return false;
	}

	/*public static void removeNonceFromStorage(final HttpServletRequest req) {
        final String stateFromStorage = SessionUtils.getState() != null ? SessionUtils.getState() : "";
        final String stateFromStorageWithoutNonce = "";//removeFromQueryParams(stateFromStorage, NONCE_KEY);
        SessionUtils.setState(req, stateFromStorageWithoutNonce);
    }

    public static boolean matchesNonceInStorage(final HttpServletRequest req, final String stateFromRequest) {
        final String nonceFromRequest ="";// parseFromQueryParams(stateFromRequest, NONCE_KEY);
        final String stateFromStorage = SessionUtils.getState(req);
        final String nonceFromStorage = "";//parseFromQueryParams(stateFromStorage, NONCE_KEY);
        return nonceFromRequest != null && !nonceFromRequest.isEmpty() && nonceFromRequest.equals(nonceFromStorage);
    }*/

}