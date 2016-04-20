package io.skysail.server.app.webconsole.bundles.test;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Reference;
import org.restlet.security.Authenticator;

import io.skysail.api.um.AuthenticationService;
import io.skysail.api.um.AuthorizationService;
import io.skysail.api.validation.ValidatorService;
import io.skysail.server.SkysailComponent;
import io.skysail.server.app.ServiceListProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.TranslationRenderServiceHolder;
import io.skysail.server.app.webconsole.WebconsoleApplication;
import io.skysail.server.app.webconsole.bundles.BundlesResource;
import io.skysail.server.app.webconsole.osgi.OsgiService;
import io.skysail.server.services.PerformanceMonitor;
import io.skysail.server.text.TranslationStoreHolder;

@RunWith(MockitoJUnitRunner.class)
public class BundlesResourceTest {

	@Mock
	private OsgiService osgiService;
	
	@Mock
	private ServiceListProvider serviceListProvider;
	
	@Mock
	private AuthenticationService authService;
	
	@Mock
	private Authenticator authenticator;

	@Mock
	private Request request;

	@Mock
	private Reference resourceRef;

	private Context context;

	private BundlesResource bundlesResource;

	@Before
	public void setup() throws Exception {
		context = new Context();
		WebconsoleApplication application = new WebconsoleApplication();
		Mockito.when(authService.getApplicationAuthenticator(context)).thenReturn(authenticator);
		Mockito.when(serviceListProvider.getAuthenticationService()).thenReturn(authService);
		SkysailApplication.setServiceListProvider(serviceListProvider);
		application.setContext(context);
		application.createInboundRoot();
		Field osgiServiceField = WebconsoleApplication.class.getDeclaredField("osgiService");
		osgiServiceField.setAccessible(true);
		osgiServiceField.set(application, osgiService);
		org.restlet.Application.setCurrent(application);
		bundlesResource = new BundlesResource();
		Mockito.when(request.getResourceRef()).thenReturn(resourceRef);
		bundlesResource.setRequest(request);
	}
	
	@Test
	public void getEntity_delegates_to_osgiService() {
		bundlesResource.init(context, request, new Response(request));
		bundlesResource.getEntity();
		Mockito.verify(osgiService).getBundleDescriptors();
	}
	 
	@Test
	public void getLinks_returns_values() {
		assertThat(bundlesResource.getLinks().size(), greaterThan(0));
	}

}
