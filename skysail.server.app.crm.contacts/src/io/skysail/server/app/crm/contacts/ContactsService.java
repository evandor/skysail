package io.skysail.server.app.crm.contacts;

import java.util.List;
import java.util.Optional;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.restlet.Request;

import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.crm.contacts.repositories.ContactsRepo;
import io.skysail.server.db.DbService;
import io.skysail.server.queryfilter.filtering.Filter;

@Component(immediate = true, service = ContactsService.class)
public class ContactsService {
    
    @Reference
    private DbService dbService;

    private Optional<SkysailApplication> app;

    private ContactsRepo contactsRepo;
    
//    @Reference
//    private ApplicationListProvider applications;
    
    @Activate
    public void activate() {
        this.contactsRepo = new ContactsRepo(dbService);
//        this.app = applications.getApplications().stream()
//                .map(a -> {
//                    System.out.println(a.getName());
//                    return a;
//                })
//                .filter(a -> a.getName().equals("contacts")).findFirst();
    }
    
    List<Contact> getContacts() {
        return contactsRepo.find(new Filter(new Request()));
    }

}
