package io.skysail.server.restlet.resources;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import io.skysail.api.links.LinkRelation;
import io.skysail.domain.Identifiable;
import io.skysail.server.app.SkysailApplication;

@RunWith(MockitoJUnitRunner.class)
public class SkysailServerResourceTest {

    private TestSkysailServerResource serverResource;

    private class TestSkysailServerResource extends SkysailServerResource<Identifiable> {

        @Override
        public LinkRelation getLinkRelation() {
            return LinkRelation.ABOUT;
        }

        @Override
        public Identifiable getEntity() {
            return null;
        }

        @Override
        public SkysailApplication getApplication() {
            return Mockito.mock(SkysailApplication.class);
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
        assertThat(serverResource.getEntityType(), is(equalTo(Identifiable.class.getSimpleName())));
    }

}
