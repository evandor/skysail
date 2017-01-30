package io.skysail.server.app.crm.addresses;

import java.util.Arrays;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;

import io.skysail.server.app.ApiVersion;
import io.skysail.server.app.ApplicationConfiguration;
import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.crm.addresses.repositories.Repository;
import io.skysail.server.db.DbService;
import io.skysail.server.menus.MenuItemProvider;
import lombok.Getter;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class AddressesApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

	public static final String APP_NAME = "addresses";

	@Reference
	private DbService dbService;

	@Reference
	private AddressesAPI addressesAPI;

	@Getter
	private Repository repository;

	public AddressesApplication() {
		super(APP_NAME, new ApiVersion(1), Arrays.asList(Address.class));
		setDescription("a skysail application");
	}

	@Activate
	@Override
	public void activate(ApplicationConfiguration appConfig, ComponentContext componentContext)
			throws ConfigurationException {
		super.activate(appConfig, componentContext);
		this.repository = new Repository(dbService);
	}

	public Address augmentWithApiKey(Address address) {
		return addressesAPI.augmentWithApiKey(address);
	}

}