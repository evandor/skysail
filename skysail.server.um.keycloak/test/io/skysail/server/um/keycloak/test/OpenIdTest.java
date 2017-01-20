package io.skysail.server.um.keycloak.test;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;

import org.junit.Ignore;
import org.junit.Test;

import com.nimbusds.oauth2.sdk.AuthorizationCode;
import com.nimbusds.oauth2.sdk.ResponseType;
import com.nimbusds.oauth2.sdk.Scope;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.oauth2.sdk.id.State;
import com.nimbusds.openid.connect.sdk.AuthenticationErrorResponse;
import com.nimbusds.openid.connect.sdk.AuthenticationRequest;
import com.nimbusds.openid.connect.sdk.AuthenticationResponse;
import com.nimbusds.openid.connect.sdk.AuthenticationResponseParser;
import com.nimbusds.openid.connect.sdk.AuthenticationSuccessResponse;
import com.nimbusds.openid.connect.sdk.Nonce;
import com.nimbusds.openid.connect.sdk.op.OIDCProviderMetadata;

@Ignore
public class OpenIdTest {

	@Test
	public void testName() throws Exception {
		ClientID clientID = new ClientID("skysail");

		// The client callback URL
		URL callback = new URL("http://localhost:2020/io.skysail.server.um.shiro.app.ShiroUmApplication/v1/_login");

		// Generate random state string for pairing the response to the request
		State state = new State();

		// Generate nonce
		Nonce nonce = new Nonce();

		// redirect_uri=http://localhost:2020/io.skysail.server.um.shiro.app.ShiroUmApplication/v1/_login
		
		// Compose the request (in code flow)
		AuthenticationRequest req = new AuthenticationRequest(
		    new URL("http://localhost:8080/auth/realms/demo/protocol/openid-connect/auth?redirect_uri=http://localhost:2020/io.skysail.server.um.shiro.app.ShiroUmApplication/v1/_login").toURI(),
		    new ResponseType("code"),
		    Scope.parse("openid email profile address"),
		    clientID,
		    callback.toURI(),
		    state,
		    nonce);

		HTTPResponse httpResponse = req.toHTTPRequest().send();

		AuthenticationResponse response = AuthenticationResponseParser.parse(httpResponse);

		if (response instanceof AuthenticationErrorResponse) {
		    // process error
		}

		AuthenticationSuccessResponse successResponse =
		    (AuthenticationSuccessResponse)response;

		// Retrieve the authorisation code
		AuthorizationCode code = successResponse.getAuthorizationCode();

		// Don't forget to check the state
		assert successResponse.getState().equals(state);
	}
	
	@Test
	public void testName2() throws Exception {
		URI issuerURI = new URI("http://localhost:8080/auth/realms/demo/");
		URL providerConfigurationURL = issuerURI.resolve("http://localhost:8080/auth/realms/demo/.well-known/openid-configuration").toURL();
		InputStream stream = providerConfigurationURL.openStream();
		// Read all data from URL
		String providerInfo = null;
		try (java.util.Scanner s = new java.util.Scanner(stream)) {
		  providerInfo = s.useDelimiter("\\A").hasNext() ? s.next() : "";
		}
		OIDCProviderMetadata providerMetadata = OIDCProviderMetadata.parse(providerInfo);
		System.out.println(providerMetadata);
	}
}
