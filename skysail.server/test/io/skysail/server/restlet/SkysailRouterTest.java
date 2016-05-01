package io.skysail.server.restlet;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.resource.Finder;
import org.restlet.resource.ServerResource;
import org.restlet.routing.Filter;

import io.skysail.domain.core.ApplicationModel;
import io.skysail.server.app.ApiVersion;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.domain.jvm.JavaApplicationModel;

@RunWith(MockitoJUnitRunner.class)
public class SkysailRouterTest {

	public class AFinder extends Finder {
	    @Override
	    public Class<? extends ServerResource> getTargetClass() {
	        return TestServerResource.class;
	    }
    }

    public class AFilter extends Filter {
	    @Override
	    public Restlet getNext() {
	        return new AFinder();
	    }
    }

    @InjectMocks
	private SkysailRouter skysailRouter;

	@Mock
	private Context context;

	@Mock
	private SkysailApplication skysailApplication;

	@Test
	@Ignore
    public void can_retrieve_attached_routeBuilder_by_its_pathname() throws Exception {
	    Mockito.when(skysailApplication.getApplicationModel()).thenReturn(new JavaApplicationModel("id"));
	    RouteBuilder routeBuilder = new RouteBuilder("/path", TestServerResource.class);
		skysailRouter.attach(routeBuilder);

	    assertThat(skysailRouter.getRouteBuilder("/path").getPathTemplate(new ApiVersion(1)), is("/v1/path"));
	    assertThat(skysailRouter.getRouteBuilders().size(), is(1));
	    assertThat(skysailRouter.getRouteBuildersForResource(TestServerResource.class).get(0),is(routeBuilder));
	    assertThat(skysailRouter.getTemplatePathForResource(TestServerResource.class).get(0), is("/path"));
    }

	@Test
    public void testName() {
	    Filter myFilter = new AFilter();
        RouteBuilder routeBuilder = new RouteBuilder("/path", myFilter);
        skysailRouter.attach(routeBuilder);

        assertThat(skysailRouter.getRouteBuildersForResource(TestServerResource.class).get(0),is(routeBuilder));

    }
}
