package io.skysail.server.app.ref.singleentity.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.app.ref.singleentity.Account;
import io.skysail.server.app.ref.singleentity.SingleEntityApplication;
import io.skysail.server.queryfilter.filtering.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;

public class AccountsResource extends ListServerResource<Account> {

    private SingleEntityApplication app;

    public AccountsResource() {
        super(AccountResource.class);
    }

    @Override
    protected void doInit() {
        app = (SingleEntityApplication) getApplication();
    }

    @Override
    public List<Account> getEntity() {
        Filter filter = new Filter(getRequest());
        Pagination pagination = new Pagination(getRequest(), getResponse());
        return app.getRepo().find(filter, pagination);
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(PostAccountResource.class);
    }

}
