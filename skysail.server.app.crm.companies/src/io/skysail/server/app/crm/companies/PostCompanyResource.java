package io.skysail.server.app.crm.companies;

import javax.annotation.Generated;

// test

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PostEntityServerResource;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.crm.companies.repositories.CompanysRepo;

public class PostCompanyResource extends PostEntityServerResource<Company> {

	private CompaniesApplication app;
    private CompanysRepo repository;

	public PostCompanyResource() {
	    addToContext(ResourceContextId.LINK_TITLE, "Create new Company");
    }

    @Override
	protected void doInit() {
		app = (CompaniesApplication)getApplication();
        repository = (CompanysRepo) app.getCompanysRepo();
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