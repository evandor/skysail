package io.skysail.server.app.ref.one2one.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.ref.one2one.Detail;
import io.skysail.server.app.ref.one2one.Master;
import io.skysail.server.app.ref.one2one.One2OneApplication;
import io.skysail.server.app.ref.one2one.One2OneRepository;
import io.skysail.server.restlet.resources.PostRelationResource2;


public class PostMasterToNewDetailRelationResource extends PostRelationResource2<Detail> {

    private One2OneApplication app;
    private String parentId;

    public PostMasterToNewDetailRelationResource() {
        addToContext(ResourceContextId.LINK_TITLE, "create new course for this timetable");
    }

    @Override
    protected void doInit() {
        app = (One2OneApplication) getApplication();
        parentId = getAttribute("id");
    }

    @Override
    public Detail createEntityTemplate() {
        return new Detail();
    }

    @Override
    public void addEntity(Detail entity) {
        Master parent = app.getRepo().findOne(parentId);
        parent.getTodos().add(entity);
        app.getRepo().save(parent, getApplication().getApplicationModel());
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(MastersDetailsResource.class, PostMasterToNewDetailRelationResource.class);
    }
}