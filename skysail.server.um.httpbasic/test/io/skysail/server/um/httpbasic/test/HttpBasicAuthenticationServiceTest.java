package io.skysail.server.um.httpbasic.test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Header;
import org.restlet.security.Authenticator;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.security.Verifier;
import org.restlet.util.Series;

import io.skysail.server.um.httpbasic.HttpBasicAuthenticationService;
import io.skysail.server.um.httpbasic.HttpBasicUserManagementProvider;

import static org.mockito.Mockito.*;

import java.util.Collection;
import java.util.HashSet;

@RunWith(MockitoJUnitRunner.class)
public class HttpBasicAuthenticationServiceTest {

	@Mock
	private HttpBasicUserManagementProvider userManagementProvider;

	@Mock
	private Request request;

	@InjectMocks
	private HttpBasicAuthenticationService httpBasicAuthenticationService;

	private Series<Header> headers;

	@Before
	public void setup() {
		headers = new Series<Header>(Header.class);
		when(request.getHeaders()).thenReturn(headers);
		Collection<Verifier> verifiers = new HashSet<>();
		verifiers.add(new Verifier() {
			@Override
			public int verify(Request request, Response response) {
				return RESULT_VALID;
			}
		});
		when(userManagementProvider.getVerifiers()).thenReturn(verifiers);
	}
	
	@Test
	public void request_without_basic_authorization_header_is_not_authenticated() {
		assertThat(httpBasicAuthenticationService.isAuthenticated(request),is(false));
	}

	@Test
	public void request_with_invalid_basic_authorization_header_is__not_authenticated() {
		headers.add("Authorization", "Basic XXXtaW46c2t5c2FXXX==");
		assertThat(httpBasicAuthenticationService.isAuthenticated(request),is(true));
	}

	@Test
	public void request_with_valid_basic_authorization_header_is_authenticated() {
		headers.add("Authorization", "Basic YWRtaW46c2t5c2FpbA==");
		assertThat(httpBasicAuthenticationService.isAuthenticated(request),is(true));
	}
	
	@Test
	public void testName() {
		Authenticator authenticator = httpBasicAuthenticationService.getAuthenticator(null);
		assertThat(authenticator,instanceOf(ChallengeAuthenticator.class));
		assertThat(((ChallengeAuthenticator)authenticator).getVerifier().verify(null, null),is(Verifier.RESULT_VALID));
	}
}
