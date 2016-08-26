package io.skysail.server.app.ref.singleentity.resources;

import java.util.Date;

import io.skysail.server.app.ref.singleentity.Account;
import io.skysail.server.restlet.resources.PostEntityServerResource;

/**
 * A PostEntityServerResource class is responsible for the code to create an
 * entity of a specific type.
 *
 */
public class PostAccountResource extends PostEntityServerResource<Account> {

   // private SingleEntityApplication app;

//    @Override
//    public void doInit() {
//        app = (SingleEntityApplication) getApplication();
//    }

    /**
     * some kind of business logic, setting the creation date.
     */
    @Override
    public Account createEntityTemplate() {
         Account account = new Account();
         account.setCreated(new Date());
         return account;
    }

//    @Override
//    public void addEntity(Account entity) {
//        OrientVertex save = (OrientVertex) app.getRepository(Account.class).save(entity, app.getApplicationModel());
//        String id = save.getId().toString();
//        entity.setId(id);
//    }

    @Override
    public String redirectTo() {
        return super.redirectTo(AccountsResource.class);
    }

}
