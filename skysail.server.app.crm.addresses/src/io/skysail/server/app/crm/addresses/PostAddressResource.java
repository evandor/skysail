package io.skysail.server.app.crm.addresses;

import javax.annotation.Generated;

// test

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PostEntityServerResource;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.crm.addresses.repositories.AddresssRepo;

@Generated("io.skysail.server.codegen.apt.processors.EntityProcessor")
public class PostAddressResource extends PostEntityServerResource<Address> {

	private AddressesApplication app;
	private AddresssRepo repository;

	public PostAddressResource() {
		addToContext(ResourceContextId.LINK_TITLE, "Create new Address");
	}

	@Override
	protected void doInit() {
		app = (AddressesApplication) getApplication();
		repository = (AddresssRepo) app.getAddresssRepo();
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