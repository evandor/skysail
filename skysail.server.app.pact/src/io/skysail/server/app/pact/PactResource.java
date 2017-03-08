package io.skysail.server.app.pact;

import java.util.List;

import io.skysail.api.doc.ApiDescription;
import io.skysail.api.doc.ApiSummary;
import io.skysail.api.doc.ApiTags;
import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ResourceContextId;
import io.skysail.server.restlet.resources.EntityServerResource;

public class PactResource extends EntityServerResource<Pact> {

    private String id;
    private PactApplication app;

    public PactResource() {
        addToContext(ResourceContextId.LINK_TITLE, "details");
        addToContext(ResourceContextId.LINK_GLYPH, "search");
    }

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (PactApplication) getApplication();
    }

    @Override
    @ApiSummary("deletes the pact resource available at this url")
    @ApiDescription("deletes by id")
    @ApiTags("Pact")
    public SkysailResponse<?> eraseEntity() {
    	app.getRepo().delete(id);
        return new SkysailResponse<>();
    }

    @Override
    @ApiSummary("returns the default pact")
    @ApiDescription("this is for testing purposes and api evolution, subject to be removed before first release")
    @ApiTags({"Pact","Testing"})
    public Pact getEntity() {
        Pact defaultPact = new Pact();
        defaultPact.setId("1");
        defaultPact.setTitle("who get's the receipt for entertainment expeses?");
        return defaultPact;
    }

	@Override
    public List<Link> getLinks() {
        return super.getLinks(PutPactResource.class, PactsResource.class);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(PactsResource.class);
    }

}