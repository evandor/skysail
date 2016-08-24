package io.skysail.client.testsupport.authentication;

import org.restlet.resource.ClientResource;

import io.skysail.client.testsupport.ApplicationClient2;

public interface AuthenticationStrategy2 {

	ClientResource login(ApplicationClient2 client, String username, String password);
	
}
