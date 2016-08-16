package io.skysail.server.app.ref.one2one.resources;

import io.skysail.server.app.ref.one2one.Master;
import io.skysail.server.app.ref.one2one.One2OneApplication;
import io.skysail.server.restlet.resources.PostEntityServerResource;

public class PostMasterResource extends PostEntityServerResource<Master> {

    private One2OneApplication app;

    @Override
    public void doInit() {
        app = (One2OneApplication) getApplication();
    }

    @Override
    public Master createEntityTemplate() {
        return new Master();
    }

    @Override
    public void addEntity(Master entity) {
        String id = app.getRepository(Master.class).save(entity, app.getApplicationModel()).toString();
        entity.setId(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(MastersResource.class);
    }

}
