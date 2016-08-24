package io.skysail.client.testsupport.authentication;

import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.MediaType;
import org.restlet.resource.ClientResource;

import io.skysail.client.testsupport.ApplicationClient2;

public class HttpBasicAuthenticationStrategy2 implements AuthenticationStrategy2 {

	@Override
	public ClientResource login(ApplicationClient2 client, String username, String password) {
        ClientResource cr = new ClientResource(client.getBaseUrl() + "/_login");
        cr.setFollowingRedirects(true);
        cr.setChallengeResponse(new ChallengeResponse(ChallengeScheme.HTTP_BASIC, username, password));
        cr.get(MediaType.TEXT_HTML);
        return cr;

	}

}
