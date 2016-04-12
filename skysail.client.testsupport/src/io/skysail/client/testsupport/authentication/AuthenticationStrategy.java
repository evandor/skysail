package io.skysail.client.testsupport.authentication;

import org.restlet.resource.ClientResource;

import io.skysail.client.testsupport.ApplicationClient;

public interface AuthenticationStrategy {

	ClientResource login(ApplicationClient<?> client, String username, String password);
	
}
