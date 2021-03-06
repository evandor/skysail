package io.skysail.server.app.ref.one2many.resources.list;

import org.restlet.resource.ResourceException;

import io.skysail.server.app.ref.one2many.One2ManyApplication;
import io.skysail.server.app.ref.one2many.TodoList;
import io.skysail.server.restlet.resources.PutEntityServerResource;

public class PutTodoListResource extends PutEntityServerResource<TodoList> {

    protected String id;
    protected One2ManyApplication app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (One2ManyApplication)getApplication();
    }

    @Override
    public void updateEntity(TodoList  entity) {
        TodoList original = getEntity();
        copyProperties(original,entity);

        app.getRepo().update(original,app.getApplicationModel());
    }

    @Override
    public TodoList getEntity() {
        return app.getRepo().findOne(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(TodoListsResource.class);
    }
}
