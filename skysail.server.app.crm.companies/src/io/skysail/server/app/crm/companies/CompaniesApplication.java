package io.skysail.server.app.crm.companies;

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
import io.skysail.server.app.crm.companies.repositories.CompanysRepo;
import io.skysail.server.db.DbService;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.security.config.SecurityConfigBuilder;
import lombok.Getter;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class CompaniesApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String APP_NAME = "companies";

    @Reference
    private DbService dbService;

    @Getter
    private CompanysRepo companysRepo;

    public CompaniesApplication() {
        super(APP_NAME, new ApiVersion(1), Arrays.asList(Company.class));
        setDescription("a skysail application");
    }

    @Activate
    @Override
    public void activate(ApplicationConfiguration appConfig, ComponentContext componentContext)
            throws ConfigurationException {
        super.activate(appConfig, componentContext);
        this.companysRepo = new CompanysRepo(dbService);
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