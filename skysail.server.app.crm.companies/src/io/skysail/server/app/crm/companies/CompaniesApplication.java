package io.skysail.server.app.crm.companies;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import io.skysail.domain.Identifiable;
import io.skysail.server.ApplicationContextId;
import io.skysail.server.app.ApiVersion;
import io.skysail.server.app.ApplicationConfiguration;
import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.crm.companies.repositories.CompanysRepo;
import io.skysail.server.app.crm.contacts.Contact;
import io.skysail.server.app.crm.contacts.ContactsService;
import io.skysail.server.db.DbService;
import io.skysail.server.domain.jvm.SkysailApplicationService;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.security.config.SecurityConfigBuilder;
import io.skysail.server.services.EntityApi;
import lombok.Getter;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class CompaniesApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String APP_NAME = "companies";

    @Reference
    private DbService dbService;
    
    @Reference
    private ContactsService contactsService;

    @Getter
    private CompanysRepo companysRepo;
    
    @Getter
    @Reference(cardinality = ReferenceCardinality.OPTIONAL, policy = ReferencePolicy.DYNAMIC)
    private volatile SkysailApplicationService skysailApplicationService;


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
        this.companysRepo = new CompanysRepo(dbService);
        setSkysailApplicationService(skysailApplicationService);
    }

    @Override
    protected void defineSecurityConfig(SecurityConfigBuilder securityConfigBuilder) {
        securityConfigBuilder
            .authorizeRequests().startsWithMatcher("").authenticated();
    }

	public void handlePost(Identifiable entity) {
		EntityApi<Contact> contactApi = (EntityApi<Contact>) skysailApplicationService.getEntityApi(Contact.class.getName());

		Company company = (Company) entity;
		List<String> contactIds = new ArrayList<>();
		company.getContacts().forEach(contact -> {
			contactApi.persist(contact);
			contactIds.add(contact.getId());
		});
		
		companysRepo.save(entity, getApplicationModel());
		
	}
//
//    @Override
//    protected void attach() {
//        super.attach();
//
//        router.attach(new RouteBuilder("/Bookmarks/{id}", BookmarkResource.class));
//        router.attach(new RouteBuilder("/Bookmarks/", PostBookmarkResource.class));
//        router.attach(new RouteBuilder("/Bookmarks/{id}/", PutBookmarkResource.class));
//        router.attach(new RouteBuilder("/Bookmarks", BookmarksResource.class));
//        router.attach(new RouteBuilder("", BookmarksResource.class));
//        
//    }

}