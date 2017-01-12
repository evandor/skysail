package io.skysail.server.app.crm.contacts;

import org.osgi.service.component.annotations.Component;

import io.skysail.domain.Identifiable;
import io.skysail.server.services.EntityApi;

@Component(immediate = true)
public class ContactsAPI implements EntityApi<Contact> {
    
    @Override
    public Class<? extends Identifiable> getEntityClass() {
        return Contact.class;
    }


    @Override
    public Contact create() {
        return new PostContactResource().createEntityTemplate();
    }

    @Override
    public void perist() {
        
    }
	
	
}
