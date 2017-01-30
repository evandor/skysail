package io.skysail.server.app.crm.contacts;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.tinkerpop.blueprints.impls.orient.OrientVertex;

import io.skysail.domain.Entity;
import io.skysail.server.app.crm.contacts.repositories.Repository;
import io.skysail.server.db.DbService;
import io.skysail.server.services.EntityApi;

@Component(immediate = true)
public class ContactsAPI implements EntityApi<Contact> {

	@Reference
    private DbService dbService;

	private Repository repository;

	@Activate
	public void activate() {
	    repository = new Repository(dbService);
	}

    @Override
    public Class<? extends Entity> getEntityClass() {
        return Contact.class;
    }


    @Override
    public Contact create() {
        return new PostContactResource().createEntityTemplate();
    }

    @Override
    public void persist(Contact entity) {
        OrientVertex save = repository.save(entity, null);
        entity.setId(save.getId().toString());
    }


}
