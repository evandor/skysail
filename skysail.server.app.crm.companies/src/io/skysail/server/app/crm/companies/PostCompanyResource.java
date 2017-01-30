package io.skysail.server.app.crm.companies;

import io.skysail.server.ResourceContextId;
import io.skysail.server.app.crm.companies.repositories.CompanyRepository;
import io.skysail.server.restlet.resources.PostEntityServerResource;

public class PostCompanyResource extends PostEntityServerResource<Company> {

	private CompaniesApplication app;
    private CompanyRepository repository;

	public PostCompanyResource() {
	    addToContext(ResourceContextId.LINK_TITLE, "Create new Company");
    }

    @Override
	protected void doInit() {
		app = (CompaniesApplication)getApplication();
        repository = (CompanyRepository) app.getRepository(Company.class);
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