package io.skysail.server.app.crm.companies;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import io.skysail.domain.Entity;
import io.skysail.server.app.crm.companies.repositories.Repository;
import io.skysail.server.app.crm.contacts.Contact;
import io.skysail.server.db.DbService;
import io.skysail.server.domain.jvm.SkysailApplicationService;
import io.skysail.server.services.EntityApi;

@Component(immediate = true)
public class CompaniesAPI implements EntityApi<Company> {

    @Reference
    private DbService dbService;

    @Reference
    private SkysailApplicationService appService;

    private Repository repository;

    @Activate
    public void activate() {
        repository = new Repository(dbService);
    }

    @Override
    public Class<? extends Entity> getEntityClass() {
        return Company.class;
    }

    @Override
    public Company create() {
        return new PostCompanyResource().createEntityTemplate();
    }

    @Override
    public void persist(Company company) {
    	// TODO transactions!
        @SuppressWarnings("unchecked")
		EntityApi<Contact> contactApi = (EntityApi<Contact>) appService.getEntityApi(Contact.class.getName());
        company.getContacts().forEach(contactApi::persist);
        repository.save(company, appService.getApplicationModel(CompaniesApplication.APP_NAME));
    }

}
