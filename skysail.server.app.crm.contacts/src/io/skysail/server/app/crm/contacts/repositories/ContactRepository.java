package io.skysail.server.app.crm.contacts.repositories;

import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.app.crm.contacts.Contact;
import io.skysail.server.db.DbClassName;
import io.skysail.server.db.DbService;
import io.skysail.server.db.GraphDbRepository;

public class ContactRepository extends GraphDbRepository<Contact> implements DbRepository {

    public ContactRepository (DbService dbService) {
        this.dbService = dbService;
        activate();
    }

    public void activate() {
        dbService.createWithSuperClass("V", DbClassName.of(Contact.class));
        dbService.register(Contact.class);
    }

}
