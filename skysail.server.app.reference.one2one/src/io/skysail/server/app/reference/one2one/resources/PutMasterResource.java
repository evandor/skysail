package io.skysail.server.app.reference.one2one.resources;

import org.restlet.resource.ResourceException;

import io.skysail.server.app.reference.one2one.One2OneApplication;
import io.skysail.server.app.reference.one2one.Master;
import io.skysail.server.restlet.resources.PutEntityServerResource;

public class PutMasterResource extends PutEntityServerResource<Master> {

    protected String id;
    protected One2OneApplication app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (One2OneApplication)getApplication();
    }

    @Override
    public void updateEntity(Master  entity) {
        Master original = getEntity();
        copyProperties(original,entity);

        app.getRepository(Master.class).update(original,app.getApplicationModel());
    }

    @Override
    public Master getEntity() {
        return (Master)app.getRepository(Master.class).findOne(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(MastersResource.class);
    }
}
