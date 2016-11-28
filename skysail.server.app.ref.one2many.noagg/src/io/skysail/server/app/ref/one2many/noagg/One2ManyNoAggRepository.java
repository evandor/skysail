package io.skysail.server.app.ref.one2many.noagg;


import org.osgi.service.component.annotations.Component;

import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.db.DbClassName;
import io.skysail.server.db.DbService;
import io.skysail.server.db.GraphDbRepository;

@Component(immediate = true, property = "name=One2ManyNoAggRepository")
public class One2ManyNoAggRepository extends GraphDbRepository<Company> implements DbRepository {

    public One2ManyNoAggRepository(DbService dbService) {
        this.dbService = dbService;
        activate();
    }

    public void activate() {
        dbService.createWithSuperClass("V",
                DbClassName.of(Company.class),
                DbClassName.of(Contact.class));
        dbService.register(
                Company.class,
                Contact.class);
    }

}