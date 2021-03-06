package io.skysail.server.app.crm.contacts;

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
import io.skysail.server.app.crm.contacts.repositories.ContactRepository;
import io.skysail.server.db.DbService;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.security.config.SecurityConfigBuilder;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class ContactsApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String APP_NAME = "contacts";

    @Reference
    private DbService dbService;

    public ContactsApplication() {
        super(APP_NAME, new ApiVersion(1), Arrays.asList(Contact.class));
        setDescription("a skysail application");
        addToAppContext(ApplicationContextId.IMG, "user");
    }

    @Activate
    @Override
    public void activate(ApplicationConfiguration appConfig, ComponentContext componentContext)
            throws ConfigurationException {
        super.activate(appConfig, componentContext);
        addRepository(new ContactRepository(dbService));
    }

    @Override
    protected void defineSecurityConfig(SecurityConfigBuilder securityConfigBuilder) {
        securityConfigBuilder
            .authorizeRequests().startsWithMatcher("").authenticated();
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