package io.skysail.server.app.ref.one2many.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.app.ref.one2many.One2ManyApplication;
import io.skysail.server.app.ref.one2many.One2ManyRepository;
import io.skysail.server.app.ref.one2many.TodoList;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;

public class TodoListsResource extends ListServerResource<TodoList> {

    private One2ManyApplication app;
    private One2ManyRepository repository;

    public TodoListsResource() {
        super(TodoListResource.class);
    }

    @Override
    protected void doInit() {
        app = (One2ManyApplication) getApplication();
        repository = (One2ManyRepository) app.getRepository(TodoList.class);
    }

    @Override
    public List<TodoList> getEntity() {
        Filter filter = new Filter(getRequest());
        Pagination pagination = new Pagination(getRequest(), getResponse());
        return repository.find(filter, pagination);
    }

    @Override
    public List<Link> getLinks() {
        // this will add a link to the "post new account" resource
        return super.getLinks(PostTodoListResource.class);
    }

}
