package io.skysail.client.testsupport.authentication;

import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.MediaType;
import org.restlet.resource.ClientResource;

import io.skysail.client.testsupport.ApplicationClient;

public class HttpBasicAuthenticationStrategy implements AuthenticationStrategy {

	@Override
	public ClientResource login(ApplicationClient<?> client, String username, String password) {
        ClientResource cr = new ClientResource(client.getBaseUrl() + "/_httpbasic");
        cr.setChallengeResponse(new ChallengeResponse(ChallengeScheme.HTTP_BASIC, username, password));
        cr.get(MediaType.TEXT_HTML);
        return cr;//.getChallengeResponse();

	}

}
