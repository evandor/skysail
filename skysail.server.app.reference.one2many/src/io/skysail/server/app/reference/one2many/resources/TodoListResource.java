package io.skysail.server.app.reference.one2many.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.reference.one2many.TodoList;
import io.skysail.server.app.reference.one2many.One2ManyApplication;
import io.skysail.server.app.reference.one2many.One2ManyRepository;
import io.skysail.server.restlet.resources.EntityServerResource;

public class TodoListResource extends EntityServerResource<TodoList> {

    private String id;
    private One2ManyApplication app;
    private One2ManyRepository repository;

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (One2ManyApplication) getApplication();
        repository = (One2ManyRepository) app.getRepository(TodoList.class);
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(PutTodoListResource.class);
    }

    @Override
    public TodoList getEntity() {
        return (TodoList) app.getRepository().findOne(id);
    }

}
