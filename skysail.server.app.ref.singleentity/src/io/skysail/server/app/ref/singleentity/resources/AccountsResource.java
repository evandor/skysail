package io.skysail.server.app.ref.singleentity.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.app.ref.singleentity.Account;
import io.skysail.server.app.ref.singleentity.SingleEntityApplication;
import io.skysail.server.app.ref.singleentity.SingleEntityRepository;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;

public class AccountsResource extends ListServerResource<Account> {

    private SingleEntityApplication app;
    private SingleEntityRepository repository;

    public AccountsResource() {
        super(AccountResource.class);
    }

    @Override
    protected void doInit() {
        app = (SingleEntityApplication) getApplication();
        repository = (SingleEntityRepository) app.getRepository(Account.class);
    }

    @Override
    public List<Account> getEntity() {
        Filter filter = new Filter(getRequest());
        Pagination pagination = new Pagination(getRequest(), getResponse(), repository.count(filter));
        return repository.find(filter, pagination);
    }

    @Override
    public List<Link> getLinks() {
        // this will add a link to the "post new account" resource
        return super.getLinks(PostAccountResource.class);
    }

}
