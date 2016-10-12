package skysail.server.app.pact;

import org.restlet.resource.ResourceException;

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
    public void updateEntity(Pact  entity) {
        Pact original = getEntity();
        copyProperties(original,entity);

        app.getRepository(Pact.class).update(original,app.getApplicationModel());
    }

    @Override
    public Pact getEntity() {
        return (Pact)app.getRepository(Pact.class).findOne(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(PactsResource.class);
    }
}