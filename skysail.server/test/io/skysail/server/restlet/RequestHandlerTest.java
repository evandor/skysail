package io.skysail.server.restlet;

import io.skysail.core.app.SkysailApplication;
import io.skysail.domain.Entity;
import io.skysail.server.restlet.filter.AbstractResourceFilter;
import io.skysail.server.restlet.resources.EntityServerResource;

import org.junit.*;
import org.mockito.Mockito;
import org.restlet.*;
import org.restlet.data.Method;

public class RequestHandlerTest {

    private RequestHandler requestHandler;
    private EntityServerResource<Entity> entityServerResource;
    private Response response;

    @Before
    public void setUp() {
        SkysailApplication application = Mockito.mock(SkysailApplication.class);
        requestHandler = new RequestHandler(application);
        entityServerResource = Mockito.mock(EntityServerResource.class);
        Request request = Mockito.mock(Request.class);
        response = new Response(request);
    }

    @Test
    @Ignore
    public void testName() {
        AbstractResourceFilter filter = requestHandler.createForEntity(Method.DELETE);
        Response result = filter.handle(entityServerResource, response).getResponse();
        // assertThat(result.getStatus(),is(Status.CLIENT_ERROR_BAD_REQUEST));
    }
}
