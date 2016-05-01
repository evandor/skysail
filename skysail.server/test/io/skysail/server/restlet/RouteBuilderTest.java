package io.skysail.server.restlet;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import io.skysail.server.TestServerResource;
import io.skysail.server.app.ApiVersion;

public class RouteBuilderTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testRouteBuilderStringRestlet() throws Exception {
        RouteBuilder routeBuilder = new RouteBuilder("/path", TestServerResource.class);
        assertThat(routeBuilder.getPathTemplate(new ApiVersion(1)), is(equalTo("/v1/path")));
        assertThat(routeBuilder.getRolesForAuthorization(), is(nullValue()));
        assertThat(routeBuilder.getPathVariables().size(),is(0));
    }

    @Test
    public void singlePathVariable() {
        RouteBuilder routeBuilder = new RouteBuilder("/path/{id}", TestServerResource.class);
        assertThat(routeBuilder.getPathVariables().size(),is(1));
        assertThat(routeBuilder.getPathVariables().get(0),is("id"));
    }

    @Test
    public void multiplePathVariables() {
        RouteBuilder routeBuilder = new RouteBuilder("/path/{id}/test/{other}/rest", TestServerResource.class);
        assertThat(routeBuilder.getPathVariables().size(),is(2));
        assertThat(routeBuilder.getPathVariables().get(0),is("id"));
        assertThat(routeBuilder.getPathVariables().get(1),is("other"));
    }

}
