package io.skysail.server.app.ref.one2many.resources.list;

import com.tinkerpop.blueprints.impls.orient.OrientVertex;

import io.skysail.server.app.ref.one2many.One2ManyApplication;
import io.skysail.server.app.ref.one2many.TodoList;
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
        OrientVertex id = app.getRepo().save(entity, getApplicationModel());
        entity.setId(id.getId().toString());
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(TodoListsResource.class);
    }


}
