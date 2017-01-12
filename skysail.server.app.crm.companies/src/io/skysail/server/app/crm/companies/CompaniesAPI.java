package io.skysail.server.app.crm.companies;

import org.osgi.service.component.annotations.Component;

import io.skysail.domain.Identifiable;
import io.skysail.server.services.EntityApi;

@Component(immediate = true)
public class CompaniesAPI implements EntityApi<Company> {

	@Override
	public Class<? extends Identifiable> getEntityClass() {
		return Company.class;
	}

	@Override
	public Company create() {
		return new PostCompanyResource().createEntityTemplate();
	}

	@Override
	public String persist(Company entity) {
		return null;
	}

}
