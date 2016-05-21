package io.skysail.server.um.httpbasic.test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import io.skysail.server.um.httpbasic.HttpBasicAuthenticationService;
import io.skysail.server.um.httpbasic.HttpBasicAuthorizationService;
import io.skysail.server.um.httpbasic.HttpBasicUserManagementProvider;

public class HttpBasicUserManagementProviderTest {

	private HttpBasicUserManagementProvider provider;

	@Before
	public void setup() {
		provider = new HttpBasicUserManagementProvider();
	}

	@Test
	public void testActivate() {
		provider.activate();
		assertThat(provider.getAuthenticationService(),instanceOf(HttpBasicAuthenticationService.class));
		assertThat(provider.getAuthorizationService(),instanceOf(HttpBasicAuthorizationService.class));
	}

	@Test
	public void testDeactivate() {
		provider.activate();
		provider.deactivate();
		assertThat(provider.getAuthenticationService(),is(nullValue()));
		assertThat(provider.getAuthorizationService(),is(nullValue()));
	}

	@Test
	public void test_get_verifiers() {
		assertThat(provider.getVerifiers().size(),is(0));
	}
}
