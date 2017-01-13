package io.skysail.server.app.crm.addresses;

import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import io.skysail.domain.Identifiable;
import io.skysail.server.app.crm.addresses.repositories.AddresssRepo;
import io.skysail.server.db.DbService;
import io.skysail.server.domain.jvm.SkysailApplicationService;
import io.skysail.server.services.EntityApi;

@Component(immediate = true, configurationPid = "google", service = {EntityApi.class, AddressesAPI.class})
public class AddressesAPI implements EntityApi<Address> {

    @Reference
    private DbService dbService;

    @Reference
    private SkysailApplicationService appService;

    private AddresssRepo addresssRepo;

	private Map<String, String> config;

    @Activate
    public void activate(Map<String, String> config) {
        this.config = config;
		addresssRepo = new AddresssRepo(dbService);
    }

    @Override
    public Class<? extends Identifiable> getEntityClass() {
        return Address.class;
    }

    @Override
    public Address create() {
        Address addressTemplate = new PostAddressResource().createEntityTemplate();
        addressTemplate.setGoogleApiKey(config.get("apiKey"));
        return addressTemplate;
    }

    @Override
    public void persist(Address entity) {
        addresssRepo.save(entity, appService.getApplicationModel(AddressesApplication.APP_NAME));
    }

	public Address augmentWithApiKey(Address address) {
		address.setGoogleApiKey(config.get("apiKey"));
		return address;

	}

}
