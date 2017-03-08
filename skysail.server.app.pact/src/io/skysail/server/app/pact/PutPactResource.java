package io.skysail.server.app.pact;

import org.restlet.resource.ResourceException;

import io.skysail.api.doc.*;
import io.skysail.server.restlet.resources.PutEntityServerResource;

public class PutPactResource extends PutEntityServerResource<Pact> {

    protected String id;
    protected PactApplication app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (PactApplication)getApplication();
    }

    @Override
    @ApiSummary("summary")
    @ApiDescription("desc")
    @ApiTags("tag")
    public void updateEntity(Pact  entity) {
        Pact original = getEntity();
        copyProperties(original,entity);

        app.getRepo().update(original,app.getApplicationModel());
    }

    @Override
    @ApiSummary("summary")
    @ApiDescription("desc")
    @ApiTags("tag")
    public Pact getEntity() {
        return app.getRepo().findOne(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(PactsResource.class);
    }
}