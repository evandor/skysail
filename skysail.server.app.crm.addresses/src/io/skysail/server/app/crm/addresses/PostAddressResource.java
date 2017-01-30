package io.skysail.server.app.crm.addresses;

import io.skysail.server.ResourceContextId;
import io.skysail.server.app.crm.addresses.repositories.Repository;
import io.skysail.server.restlet.resources.PostEntityServerResource;

public class PostAddressResource extends PostEntityServerResource<Address> {

	private AddressesApplication app;
	private Repository repository;

	public PostAddressResource() {
		addToContext(ResourceContextId.LINK_TITLE, "Create new Address");
	}

	@Override
	protected void doInit() {
		app = (AddressesApplication) getApplication();
		repository = app.getRepository();
	}

	@Override
	public Address createEntityTemplate() {
		return app.augmentWithApiKey(new Address());
	}

	@Override
	public void addEntity(Address entity) {
		String id = repository.save(entity, app.getApplicationModel()).toString();
		entity.setId(id);
	}

	@Override
	public String redirectTo() {
		return super.redirectTo(AddresssResource.class);
	}
}