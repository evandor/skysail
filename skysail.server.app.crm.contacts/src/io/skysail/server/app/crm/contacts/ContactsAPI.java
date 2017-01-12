package io.skysail.server.app.crm.contacts;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.tinkerpop.blueprints.impls.orient.OrientVertex;

import io.skysail.domain.Identifiable;
import io.skysail.server.app.crm.contacts.repositories.ContactsRepo;
import io.skysail.server.db.DbService;
import io.skysail.server.services.EntityApi;

@Component(immediate = true)
public class ContactsAPI implements EntityApi<Contact> {
	
	@Reference
    private DbService dbService;

	private ContactsRepo contactsRepo;
	
	@Activate
	public void activate() {
		contactsRepo = new ContactsRepo(dbService);
	}
	
    @Override
    public Class<? extends Identifiable> getEntityClass() {
        return Contact.class;
    }


    @Override
    public Contact create() {
        return new PostContactResource().createEntityTemplate();
    }

    @Override
    public String persist(Contact entity) {
        OrientVertex save = contactsRepo.save(entity, null);
        entity.setId(save.getId().toString());
        return null;
    }
	
	
}
