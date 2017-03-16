package io.skysail.server.app.ref.one2many.resources.list;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.app.ref.one2many.One2ManyApplication;
import io.skysail.server.app.ref.one2many.TodoList;
import io.skysail.server.app.ref.one2many.resources.PostTodoListToNewTodoRelationResource;
import io.skysail.server.app.ref.one2many.resources.TodoListsTodosResource;
import io.skysail.server.restlet.resources.EntityServerResource;

public class TodoListResource extends EntityServerResource<TodoList> {

    private String id;
    private One2ManyApplication app;

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (One2ManyApplication) getApplication();
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(PutTodoListResource.class,PostTodoListToNewTodoRelationResource.class, TodoListsTodosResource.class);
    }

    @Override
    public TodoList getEntity() {
        return app.getRepo().findOne(id);
    }

}
