package io.skysail.server.app.ref.one2many.noagg.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.ref.one2many.noagg.One2ManyNoAggApplication;
import io.skysail.server.app.ref.one2many.noagg.One2ManyNoAggRepository;
import io.skysail.server.app.ref.one2many.noagg.Contact;
import io.skysail.server.app.ref.one2many.noagg.Company;
import io.skysail.server.restlet.resources.PostRelationResource2;


public class PostTodoListToNewTodoRelationResource extends PostRelationResource2<Contact> {

    private One2ManyNoAggApplication app;
    private String parentId;

    public PostTodoListToNewTodoRelationResource() {
        addToContext(ResourceContextId.LINK_TITLE, "create new course for this timetable");
    }

    @Override
    protected void doInit() {
        app = (One2ManyNoAggApplication) getApplication();
        parentId = getAttribute("id");
    }

    @Override
    public Contact createEntityTemplate() {
        return new Contact();
    }

    @Override
    public void addEntity(Contact entity) {
        Company parent = app.getRepo().findOne(parentId);
        parent.getTodos().add(entity);
        app.getRepo().save(parent, getApplication().getApplicationModel());
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(CompanysContactsResource.class, PostTodoListToNewTodoRelationResource.class);
    }
}