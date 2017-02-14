package io.skysail.server.app.pact;

import org.restlet.resource.ResourceException;

import io.skysail.server.ResourceContextId;
import io.skysail.server.restlet.resources.PostEntityServerResource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PostPactResource extends PostEntityServerResource<Pact> {

	protected PactApplication app;

    public PostPactResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (PactApplication) getApplication();
    }

    @Override
    public Pact createEntityTemplate() {
        return new Pact();
    }

    @Override
    public void addEntity(Pact entity) {
        String id = app.getRepo().save(entity, app.getApplicationModel()).toString();
        entity.setId(id);

    }

    @Override
    public String redirectTo() {
        return super.redirectTo(PactsResource.class);
    }


}