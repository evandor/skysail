package io.skysail.server.app.crm.companies;

import io.skysail.server.ResourceContextId;
import io.skysail.server.app.crm.companies.repositories.Repository;
import io.skysail.server.restlet.resources.PostEntityServerResource;

public class PostCompanyResource extends PostEntityServerResource<Company> {

	private CompaniesApplication app;
    private Repository repository;

	public PostCompanyResource() {
	    addToContext(ResourceContextId.LINK_TITLE, "Create new Company");
    }

    @Override
	protected void doInit() {
		app = (CompaniesApplication)getApplication();
        repository = app.getRepository();
	}

	@Override
    public Company createEntityTemplate() {
	    return new Company();
    }

    @Override
    public void addEntity(Company entity) {
    	app.handlePost(entity);
        //String id = repository.save(entity, app.getApplicationModel()).toString();
        //entity.setId(id);
    }

	@Override
	public String redirectTo() {
	    return super.redirectTo(CompanysResource.class);
	}
}