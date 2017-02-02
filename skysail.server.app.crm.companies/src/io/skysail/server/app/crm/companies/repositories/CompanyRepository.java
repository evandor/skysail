package io.skysail.server.app.crm.companies.repositories;

import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.app.crm.companies.Company;
import io.skysail.server.db.DbClassName;
import io.skysail.server.db.DbService;
import io.skysail.server.db.GraphDbRepository;

public class CompanyRepository extends GraphDbRepository<Company> implements DbRepository {

    public CompanyRepository (DbService dbService) {
        this.dbService = dbService;
        activate();
    }

    public void activate() {
        dbService.createWithSuperClass("V", DbClassName.of(Company.class));
        dbService.register(Company.class);
    }

}
