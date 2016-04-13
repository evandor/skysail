package io.skysail.server.um.httpbasic.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.restlet.Request;

import io.skysail.server.um.httpbasic.HttpBasicAuthenticationService;
import io.skysail.server.um.httpbasic.HttpBasicUserManagementProvider;

@RunWith(MockitoJUnitRunner.class)
public class HttpBasicAuthenticationServiceTest {

	@Mock
	private HttpBasicUserManagementProvider userManagementProvider;
	
	@InjectMocks
	private HttpBasicAuthenticationService httpBasicAuthenticationService;

	@Test
	public void testName() throws Exception {
		Request request = Mockito.mock(Request.class);
		httpBasicAuthenticationService.isAuthenticated(request);
	}
}
