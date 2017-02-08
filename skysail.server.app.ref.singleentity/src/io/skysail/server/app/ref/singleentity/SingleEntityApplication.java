package io.skysail.server.app.ref.singleentity;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import io.skysail.core.app.SkysailApplication;
import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.ref.singleentity.resources.AccountResource;
import io.skysail.server.app.ref.singleentity.resources.AccountsResource;
import io.skysail.server.app.ref.singleentity.resources.PostAccountResource;
import io.skysail.server.app.ref.singleentity.resources.PutAccountResource;
import io.skysail.server.db.DbService;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.restlet.RouteBuilder;
import io.skysail.server.security.config.SecurityConfigBuilder;
import lombok.Getter;

/**
 * The central application class extending SkysailApplication.
 *
 * It needs to implement ApplicationProvider to be considered by the skysail server.
 */
@Component(immediate = true)
public class SingleEntityApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String APP_NAME = "refSEA"; // reference application "Single AnEntity"

    @Reference
    private DbService dbService;
    
    @Getter
    private SingleEntityRepository repo;
    
    public SingleEntityApplication() {
        super(APP_NAME);
    }

    @Activate
    public void activate(ComponentContext componentContext) throws ConfigurationException {
    	super.activate(componentContext);
    	repo = new SingleEntityRepository(dbService);
    }

    /**
     * all access to routes is restricted to authenticated users.
     */
    @Override
    protected void defineSecurityConfig(SecurityConfigBuilder securityConfigBuilder) {
        securityConfigBuilder.authorizeRequests().startsWithMatcher("").authenticated();
    }

    @Override
    protected void attach() {
        super.attach();

        router.attach(new RouteBuilder("", AccountsResource.class));

        router.attach(new RouteBuilder("/accounts", AccountsResource.class));
        router.attach(new RouteBuilder("/accounts/", PostAccountResource.class));
        router.attach(new RouteBuilder("/accounts/{id}", AccountResource.class));
        router.attach(new RouteBuilder("/accounts/{id}/", PutAccountResource.class));
    }
}
