package io.skysail.server.restlet.resources;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.restlet.data.Method;

import io.skysail.api.links.LinkRelation;
import io.skysail.core.app.SkysailApplication;
import io.skysail.core.resources.SkysailServerResource;
import io.skysail.domain.Entity;

@RunWith(MockitoJUnitRunner.class)
public class SkysailServerResourceTest {

    private TestSkysailServerResource serverResource;

    private class TestSkysailServerResource extends SkysailServerResource<Entity> {

        @Override
        public LinkRelation getLinkRelation() {
            return LinkRelation.ABOUT;
        }

        @Override
        public Entity getEntity() {
            return null;
        }

        @Override
        public SkysailApplication getApplication() {
            return Mockito.mock(SkysailApplication.class);
        }

		@Override
		public Map<Method, Map<String, Object>> getApiMetadata() {
			return null;
		}

    };

    @Before
    public void setUp() throws Exception {
        serverResource = new TestSkysailServerResource();
    }

    @Test
    public void returns_resources_linkRelation() throws Exception {
        assertThat(serverResource.getLinkRelation(), is(equalTo(LinkRelation.ABOUT)));
    }

    @Test
    public void testName() throws Exception {
        assertThat(serverResource.getEntityType(), is(equalTo(Entity.class.getSimpleName())));
    }

}
