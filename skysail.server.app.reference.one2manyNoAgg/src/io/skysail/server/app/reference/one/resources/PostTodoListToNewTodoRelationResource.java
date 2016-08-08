package io.skysail.server.app.reference.one.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.reference.one.Company;
import io.skysail.server.app.reference.one.Contact;
import io.skysail.server.app.reference.one.One2ManyNoAggApplication;
import io.skysail.server.app.reference.one.One2ManyNoAggRepository;
import io.skysail.server.restlet.resources.PostRelationResource2;


public class PostTodoListToNewTodoRelationResource extends PostRelationResource2<Contact> {

    private One2ManyNoAggApplication app;
    private One2ManyNoAggRepository repo;
    private String parentId;

    public PostTodoListToNewTodoRelationResource() {
        addToContext(ResourceContextId.LINK_TITLE, "create new course for this timetable");
    }

    @Override
    protected void doInit() {
        app = (One2ManyNoAggApplication) getApplication();
        repo = (One2ManyNoAggRepository) app.getRepository(Company.class);
        parentId = getAttribute("id");
    }

    @Override
    public Contact createEntityTemplate() {
        return new Contact();
    }

    @Override
    public void addEntity(Contact entity) {
        Company parent = repo.findOne(parentId);
        parent.getTodos().add(entity);
        repo.save(parent, getApplication().getApplicationModel());
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(TodoListsTodosResource.class, PostTodoListToNewTodoRelationResource.class);
    }
}