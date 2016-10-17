package skysail.server.app.pact;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ResourceContextId;
import io.skysail.server.restlet.resources.EntityServerResource;

public class PactResource extends EntityServerResource<Pact> {

    private String id;
    private PactApplication app;
    private PactRepository repository;

    public PactResource() {
        addToContext(ResourceContextId.LINK_TITLE, "details");
        addToContext(ResourceContextId.LINK_GLYPH, "search");
    }

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (PactApplication) getApplication();
        repository = (PactRepository) app.getRepository(Pact.class);
    }


    @Override
    public SkysailResponse<?> eraseEntity() {
        repository.delete(id);
        return new SkysailResponse<>();
    }

    @Override
    public Pact getEntity() {
        //return (Pact)app.getRepository().findOne(id);
        Pact defaultPact = new Pact();
        defaultPact.setId("1");
        defaultPact.setName("who get's the receipt for entertainment expeses?");
        return defaultPact;
    }

	@Override
    public List<Link> getLinks() {
        return super.getLinks(PutPactResource.class);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(PactsResource.class);
    }


}