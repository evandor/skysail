package io.skysail.server.app.crm.companies;

import java.util.ArrayList;
import java.util.List;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import io.skysail.domain.Identifiable;
import io.skysail.server.app.crm.companies.repositories.CompanysRepo;
import io.skysail.server.app.crm.contacts.Contact;
import io.skysail.server.db.DbService;
import io.skysail.server.domain.jvm.SkysailApplicationService;
import io.skysail.server.services.EntityApi;

@Component(immediate = true)
public class CompaniesAPI implements EntityApi<Company> {

    @Reference
    private DbService dbService;

    @Reference
    private SkysailApplicationService skysailApplicationService;

    private CompanysRepo companysRepo;

    private EntityApi<Contact> contactApi;

    @Activate
    public void activate() {
        companysRepo = new CompanysRepo(dbService);
        contactApi = (EntityApi<Contact>) skysailApplicationService.getEntityApi(Contact.class.getName());

    }

    @Override
    public Class<? extends Identifiable> getEntityClass() {
        return Company.class;
    }

    @Override
    public Company create() {
        return new PostCompanyResource().createEntityTemplate();
    }

    @Override
    public String persist(Company company) {
        List<String> contactIds = new ArrayList<>();
        company.getContacts().forEach(contact -> {
            contactApi.persist(contact);
            contactIds.add(contact.getId());
        });
        companysRepo.save(company, null);
        return company.getId();
    }

}
