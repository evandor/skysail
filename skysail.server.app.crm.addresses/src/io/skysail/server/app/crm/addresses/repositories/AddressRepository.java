package io.skysail.server.app.crm.addresses.repositories;

import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.app.crm.addresses.Address;
import io.skysail.server.db.DbClassName;
import io.skysail.server.db.DbService;
import io.skysail.server.db.GraphDbRepository;

public class AddressRepository extends GraphDbRepository<Address> implements DbRepository {

    public AddressRepository (DbService dbService) {
        this.dbService = dbService;
        activate();
    }

    public void activate() {
        dbService.createWithSuperClass("V", DbClassName.of(Address.class));
        dbService.register(Address.class);
    }

}
