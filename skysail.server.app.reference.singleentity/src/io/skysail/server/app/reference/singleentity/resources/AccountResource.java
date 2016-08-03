package io.skysail.server.app.reference.singleentity.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.reference.singleentity.Account;
import io.skysail.server.app.reference.singleentity.SingleEntityApplication;
import io.skysail.server.app.reference.singleentity.SingleEntityRepository;
import io.skysail.server.restlet.resources.EntityServerResource;

public class AccountResource extends EntityServerResource<Account> {

	private String id;
	private SingleEntityApplication app;
	private SingleEntityRepository repository;

	@Override
	protected void doInit() {
		id = getAttribute("id");
		app = (SingleEntityApplication) getApplication();
		repository = (SingleEntityRepository) app.getRepository(Account.class);
	}

	@Override
	public SkysailResponse<?> eraseEntity() {
		return null;
	}

	@Override
	public Account getEntity() {
		 return (Account)app.getRepository().findOne(id);
	}

}
