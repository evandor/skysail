package io.skysail.server.app.reference.one2many.resources;

import io.skysail.server.app.reference.one2many.TodoList;
import io.skysail.server.app.reference.one2many.One2ManyApplication;
import io.skysail.server.restlet.resources.PostEntityServerResource;

public class PostTodoListResource extends PostEntityServerResource<TodoList> {

    private One2ManyApplication app;

    @Override
    public void doInit() {
        app = (One2ManyApplication) getApplication();
    }

    @Override
    public TodoList createEntityTemplate() {
        return new TodoList();
    }

    @Override
    public void addEntity(TodoList entity) {
        String id = app.getRepository(TodoList.class).save(entity, app.getApplicationModel()).toString();
        entity.setId(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(TodoListsResource.class);
    }

}
