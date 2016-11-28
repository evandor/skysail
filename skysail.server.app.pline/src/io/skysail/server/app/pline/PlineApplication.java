package io.skysail.server.app.pline;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;

import io.skysail.domain.Identifiable;
import io.skysail.domain.core.repos.Repository;
import io.skysail.server.app.ApiVersion;
import io.skysail.server.app.ApplicationConfiguration;
import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.pline.resources.PostConfirmationResource;
import io.skysail.server.app.pline.resources.PostRegistrationResource;
import io.skysail.server.app.pline.resources.PutRegistrationCodeResource;
import io.skysail.server.app.pline.resources.PutRegistrationResource;
import io.skysail.server.app.pline.resources.RegistrationResource;
import io.skysail.server.app.pline.resources.RegistrationsResource;
import io.skysail.server.db.DbService;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.restlet.RouteBuilder;
import io.skysail.server.security.config.SecurityConfigBuilder;
import lombok.Getter;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class PlineApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String LIST_ID = "lid";
    public static final String TODO_ID = "id";
    public static final String APP_NAME = "pline";

    @Reference
    private DbService dbService;

    @Getter
    private PlineRepository repo;

    public PlineApplication() {
        super(APP_NAME, new ApiVersion(1));
        setDescription("a skysail application");
    }

    @Activate
    @Override
    public void activate(ApplicationConfiguration appConfig, ComponentContext componentContext)
            throws ConfigurationException {
        super.activate(appConfig, componentContext);
        repo = new PlineRepository(dbService);
    }

    @Override
    protected void defineSecurityConfig(SecurityConfigBuilder securityConfigBuilder) {
        securityConfigBuilder
                .authorizeRequests().startsWithMatcher("").authenticated();
    }

    @Override
    protected void attach() {
        super.attach();

        router.attach(new RouteBuilder("/registrations/{id}", RegistrationResource.class));
        router.attach(new RouteBuilder("/registrations/", PostRegistrationResource.class));
        router.attach(new RouteBuilder("/registrations/{id}/", PutRegistrationResource.class));
        router.attach(new RouteBuilder("/registrations", RegistrationsResource.class));

        router.attach(new RouteBuilder("/registrations/{id}/code", PutRegistrationCodeResource.class));
        router.attach(new RouteBuilder("/registrations/{id}/confirmations/", PostConfirmationResource.class));

        router.attach(new RouteBuilder("/registrations/{id}/followers", RegistrationsFollowersResource.class));


        router.attach(new RouteBuilder("", RegistrationsResource.class));
    }

}