package io.skysail.server.app.reference.one2many.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.app.reference.one2many.One2ManyApplication;
import io.skysail.server.app.reference.one2many.One2ManyRepository;
import io.skysail.server.app.reference.one2many.Todo;
import io.skysail.server.app.reference.one2many.TodoList;
import io.skysail.server.db.DbClassName;
import io.skysail.server.restlet.resources.ListServerResource;

public class TodosResource extends ListServerResource<Todo> {

    private One2ManyApplication app;
    private One2ManyRepository repository;

    public TodosResource() {
        super(TodoListResource.class);
    }

    @Override
    protected void doInit() {
        app = (One2ManyApplication) getApplication();
        repository = (One2ManyRepository) app.getRepository(TodoList.class);
    }

    @Override
    public List<?> getEntity() {
       //return repository.find(new Filter(getRequest()));
        String sql = "SELECT from " + DbClassName.of(Todo.class) + " WHERE #" + getAttribute("id") + " IN in('todos')";
        return null;//((SpaceRepository)app.getRepository(Space.class)).execute(Course.class, sql);
    }


    @Override
    public List<Link> getLinks() {
        // this will add a link to the "post new account" resource
        return super.getLinks(PostTodoListResource.class);
    }

}
