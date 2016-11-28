package io.skysail.server.app.ref.one2many.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.ref.one2many.One2ManyApplication;
import io.skysail.server.app.ref.one2many.One2ManyRepository;
import io.skysail.server.app.ref.one2many.Todo;
import io.skysail.server.app.ref.one2many.TodoList;
import io.skysail.server.restlet.resources.PostRelationResource2;


public class PostTodoListToNewTodoRelationResource extends PostRelationResource2<Todo> {

    private One2ManyApplication app;
    private String parentId;

    public PostTodoListToNewTodoRelationResource() {
        addToContext(ResourceContextId.LINK_TITLE, "create new course for this timetable");
    }

    @Override
    protected void doInit() {
        app = (One2ManyApplication) getApplication();
        parentId = getAttribute("id");
    }

    @Override
    public Todo createEntityTemplate() {
        return new Todo();
    }

    @Override
    public void addEntity(Todo entity) {
        TodoList parent = app.getRepo().findOne(parentId);
        parent.getTodos().add(entity);
        app.getRepo().save(parent, getApplication().getApplicationModel());
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(TodoListsTodosResource.class, PostTodoListToNewTodoRelationResource.class);
    }
}