package io.skysail.server.app.ref.singleentity.resources;

import java.security.Principal;
import java.util.Date;

import com.tinkerpop.blueprints.impls.orient.OrientVertex;

import io.skysail.domain.Identifiable;
import io.skysail.server.app.ref.singleentity.Account;
import io.skysail.server.app.ref.singleentity.SingleEntityApplication;
import io.skysail.server.restlet.resources.PostEntityServerResource;

/**
 * A PostEntityServerResource class is responsible for the code to create an
 * entity of a specific type.
 *
 */
public class PostAccountResource extends PostEntityServerResource<Account> {

    private SingleEntityApplication app;

    @Override
    public void doInit() {
        app = (SingleEntityApplication) getApplication();
    }

    /**
     * some kind of business logic, setting the creation date.
     */
    @Override
    public Account createEntityTemplate() {
         return new Account();
    }
    
    @Override
    protected void afterPost(Identifiable identifiable) {
        Account account = (Account)identifiable;
        account.setCreated(new Date());
        Principal principal = getApplication().getAuthenticationService().getPrincipal(getRequest());
        account.setOwner(principal.getName());
    }

    @Override
    public void addEntity(Account entity) {
        OrientVertex id = app.getRepo().save(entity, getApplicationModel());
        entity.setId(id.getId().toString());
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(AccountsResource.class);
    }


}
