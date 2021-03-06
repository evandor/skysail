package io.skysail.server.app.ref.one2one.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.app.ref.one2one.Master;
import io.skysail.server.app.ref.one2one.One2OneApplication;
import io.skysail.server.restlet.resources.EntityServerResource;

public class MasterResource extends EntityServerResource<Master> {

    private String id;
    private One2OneApplication app;

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (One2OneApplication) getApplication();
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(PutMasterResource.class,PostMasterToNewDetailRelationResource.class, MastersDetailsResource.class);
    }

    @Override
    public Master getEntity() {
        return (Master) app.getRepository().findOne(id);
    }

}
