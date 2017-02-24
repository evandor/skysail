package io.skysail.api.um.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.mockito.Mockito;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.ClientInfo;

import io.skysail.api.um.AlwaysAuthenticatedAuthenticator;

public class AlwaysAuthenticatedAuthenticatorTest {

	@Test
	public void authenticates_always() {
		AlwaysAuthenticatedAuthenticator authenticator = new AlwaysAuthenticatedAuthenticator(null);
		Request request = Mockito.mock(Request.class);
		Response response = Mockito.mock(Response.class);
		ClientInfo clientInfo = new ClientInfo();
		Mockito.when(request.getClientInfo()).thenReturn(clientInfo);
		authenticator.handle(request, response);
		assertThat(clientInfo.isAuthenticated(),is(true));
	}
}
