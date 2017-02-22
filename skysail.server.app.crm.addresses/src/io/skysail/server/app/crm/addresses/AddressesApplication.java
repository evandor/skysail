package io.skysail.server.app.crm.addresses;

import java.util.Arrays;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;

import io.skysail.core.app.ApiVersion;
import io.skysail.core.app.ApplicationConfiguration;
import io.skysail.core.app.ApplicationProvider;
import io.skysail.core.app.SkysailApplication;
import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.app.crm.addresses.repositories.AddressRepository;
import io.skysail.server.db.DbService;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.security.config.SecurityConfigBuilder;
import lombok.Getter;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class AddressesApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

	public static final String APP_NAME = "addresses";

	@Reference
	private DbService dbService;

	@Reference
	private AddressesAPI addressesAPI;

	@Getter
    private DbRepository repository;

	public AddressesApplication() {
		super(APP_NAME, new ApiVersion(1), Arrays.asList(Address.class));
		setDescription("addresses management features for skysail applicaitons based on googles maps autocomplete");
	}

	@Activate
	@Override
	public void activate(ApplicationConfiguration appConfig, ComponentContext componentContext)
			throws ConfigurationException {
		super.activate(appConfig, componentContext);
		addRepository(new AddressRepository(dbService));
	}

	@Override
    protected void defineSecurityConfig(SecurityConfigBuilder securityConfigBuilder) {
		securityConfigBuilder.authorizeRequests().startsWithMatcher("").authenticated();
	}

	public Address augmentWithApiKey(Address address) {
		return addressesAPI.augmentWithApiKey(address);
	}

}