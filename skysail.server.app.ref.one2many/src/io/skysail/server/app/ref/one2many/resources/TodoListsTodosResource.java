package io.skysail.server.app.ref.one2many.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.ref.one2many.One2ManyApplication;
import io.skysail.server.app.ref.one2many.Todo;
import io.skysail.server.db.DbClassName;
import io.skysail.server.restlet.resources.ListServerResource;


public class TodoListsTodosResource extends ListServerResource<Todo> {

    private One2ManyApplication app;

    public TodoListsTodosResource() {
        super(TodoListsTodoResource.class);
        addToContext(ResourceContextId.LINK_GLYPH, "list");
        addToContext(ResourceContextId.LINK_TITLE, "list courses for this timetable");
    }

    @Override
    protected void doInit() {
        app = (One2ManyApplication) getApplication();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Todo> getEntity() {
        String sql = "select * from " + DbClassName.of(Todo.class) + " where #"+getAttribute("id")+" in IN(todos)";
        return (List<Todo>) app.getRepo().execute(Todo.class, sql);
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(TodoListsTodosResource.class, PostTodoListToNewTodoRelationResource.class);
    }
}