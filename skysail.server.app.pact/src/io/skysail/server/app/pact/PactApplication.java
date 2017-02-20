package io.skysail.server.app.pact;

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
import io.skysail.server.app.pact.resources.ConfirmationsResource;
import io.skysail.server.app.pact.resources.PostConfirmationResource;
import io.skysail.server.db.DbService;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.restlet.RouteBuilder;
import io.skysail.server.security.config.SecurityConfigBuilder;
import lombok.Getter;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class PactApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String LIST_ID = "lid";
    public static final String TODO_ID = "id";
    public static final String APP_NAME = "pact";

    @Reference
    private DbService dbService;

    @Getter
    private PactRepository repo;

    @Getter
	private ConfirmationRepository confRepo;

    public PactApplication() {
        super(APP_NAME, new ApiVersion(1));
        setDescription("The skysail demo application");
    }

    @Activate
    @Override
    public void activate(ApplicationConfiguration appConfig, ComponentContext componentContext)
            throws ConfigurationException {
        super.activate(appConfig, componentContext);
        this.repo = new PactRepository(dbService);
        this.confRepo = new ConfirmationRepository(dbService);
    }

    @Override
    protected void defineSecurityConfig(SecurityConfigBuilder securityConfigBuilder) {
        securityConfigBuilder
            .authorizeRequests().equalsMatcher("/Bookmarks/").permitAll().and()
            .authorizeRequests().startsWithMatcher("/unprotected").permitAll().and()
            .authorizeRequests().startsWithMatcher("").permitAll();
//            .authorizeRequests().startsWithMatcher("").authenticated();
    }

    @Override
    protected void attach() {
        super.attach();

        router.attach(new RouteBuilder("", PactResource.class));

        router.attach(new RouteBuilder("/pacts", PactsResource.class));
        router.attach(new RouteBuilder("/pacts/", PostPactResource.class));
        router.attach(new RouteBuilder("/pacts/{id}", PactResource.class));

        router.attach(new RouteBuilder("/pact/", PutPactResource.class));
        router.attach(new RouteBuilder("/pact", PactResource.class));

        router.attach(new RouteBuilder("/turn", NextCandiateResource.class));

        router.attach(new RouteBuilder("/confirmations/", PostConfirmationResource.class));
        router.attach(new RouteBuilder("/confirmations", ConfirmationsResource.class));

    }

}