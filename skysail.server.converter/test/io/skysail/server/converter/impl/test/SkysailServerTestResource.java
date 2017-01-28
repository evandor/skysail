package io.skysail.server.converter.impl.test;

import org.mockito.Mockito;
import org.osgi.framework.Bundle;
import org.restlet.Request;
import org.restlet.data.Reference;

import io.skysail.api.links.LinkRelation;
import io.skysail.domain.Entity;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.resources.SkysailServerResource;

public class SkysailServerTestResource extends SkysailServerResource<Entity> {

    public SkysailServerTestResource() {
        setApplication(new SkysailApplication("theApp") {

            private Bundle bundle = Mockito.mock(Bundle.class);

            @Override
            public Bundle getBundle() {
                return this.bundle;
            }
        });
    }

    @Override
    public Entity getEntity() {
        return null;
    }

    @Override
    public LinkRelation getLinkRelation() {
        return null;
    }

    @Override
    public Request getRequest() {
        return new Request() {
            @Override
            public Reference getOriginalRef() {
                return new Reference();
            }
        };
    }

    @Override
    public Reference getHostRef() {
        return new Reference() {
            @Override
            public String getHostDomain() {
                return "localhost";
            }
        };
    }

}
