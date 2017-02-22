package io.skysail.server.app.crm.companies;

import java.util.Arrays;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;

import io.skysail.core.app.ApiVersion;
import io.skysail.core.app.ApplicationConfiguration;
import io.skysail.core.app.ApplicationContextId;
import io.skysail.core.app.ApplicationProvider;
import io.skysail.core.app.SkysailApplication;
import io.skysail.domain.Entity;
import io.skysail.server.app.crm.companies.repositories.CompanyRepository;
import io.skysail.server.app.crm.contacts.ContactsService;
import io.skysail.server.db.DbService;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.security.config.SecurityConfigBuilder;
import io.skysail.server.services.EntityApi;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class CompaniesApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String APP_NAME = "companies";

    @Reference
    private DbService dbService;

    @Reference
    private ContactsService contactsService;

    private EntityApi<Company> companiesApi;


    public CompaniesApplication() {
        super(APP_NAME, new ApiVersion(1), Arrays.asList(Company.class));
        setDescription("a skysail application");
        addToAppContext(ApplicationContextId.IMG, "industry");
    }

    @Activate
    @Override
    public void activate(ApplicationConfiguration appConfig, ComponentContext componentContext)
            throws ConfigurationException {
        super.activate(appConfig, componentContext);
        addRepository(new CompanyRepository(dbService));
//        setSkysailApplicationService(skysailApplicationService);
//        companiesApi = (EntityApi<Company>) skysailApplicationService.getEntityApi(Company.class.getName());
    }

    @Override
    protected void defineSecurityConfig(SecurityConfigBuilder securityConfigBuilder) {
        securityConfigBuilder
            .authorizeRequests().startsWithMatcher("").authenticated();
    }

	public void handlePost(Entity entity) {
	    companiesApi.persist((Company)entity);
	}

    @Override
    protected void attach() {
        super.attach();


    }

}