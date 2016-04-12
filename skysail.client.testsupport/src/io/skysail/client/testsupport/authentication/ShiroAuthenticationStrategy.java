package io.skysail.client.testsupport.authentication;

import org.restlet.resource.ClientResource;

import io.skysail.client.testsupport.ApplicationClient;

public class ShiroAuthenticationStrategy implements AuthenticationStrategy {

	@Override
	public ClientResource login(ApplicationClient<?> client, String username, String password) {
		return null;
	}

}
