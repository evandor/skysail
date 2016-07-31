package io.skysail.server.um.auth0;

import org.apache.commons.lang3.Validate;

import com.auth0.Auth0;
import com.auth0.authentication.AuthenticationAPIClient;
import com.auth0.authentication.result.Credentials;
import com.auth0.authentication.result.UserProfile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Wrapper implementation around Auth0 service calls
 * Don't expose internals of Auth0 library
 */
//@Service
public class Auth0ClientImpl implements Auth0Client {

    protected Auth0BundleConfig auth0Config;
    protected AuthenticationAPIClient authenticationAPIClient;

    //@Autowired
    public Auth0ClientImpl(final Auth0BundleConfig auth0Config) {
        Validate.notNull(auth0Config);
        this.auth0Config = auth0Config;
        Auth0 auth0 = new Auth0(auth0Config.clientId(), auth0Config.clientSecret(), auth0Config.domain());
        authenticationAPIClient = new AuthenticationAPIClient(auth0);
    }

    @Override
    public Tokens getTokens(final String authorizationCode, final String redirectUri) {
        Validate.notNull(authorizationCode);
        Validate.notNull(redirectUri);
        Gson gson = new GsonBuilder().registerTypeAdapter(Credentials.class, new InterfaceAdapter<Credentials>())
                .create();
        final Credentials creds = authenticationAPIClient
                .token(authorizationCode, redirectUri)
                .setClientSecret(auth0Config.clientSecret()).execute();
        return new Tokens(creds.getIdToken(), creds.getAccessToken(), creds.getType(), creds.getRefreshToken());
    }

    @Override
    public Auth0User getUserProfile(final Tokens tokens) {
        Validate.notNull(tokens);
        final String idToken = tokens.getIdToken();
        final UserProfile userProfile = authenticationAPIClient.tokenInfo(idToken).execute();
        return new Auth0User(userProfile);
    }

}