package io.skysail.server.app.pline;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.event.EventAdmin;

import io.skysail.domain.core.Repositories;
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
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.restlet.RouteBuilder;
import io.skysail.server.security.config.SecurityConfigBuilder;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class PlineApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String LIST_ID = "lid";
    public static final String TODO_ID = "id";
    public static final String APP_NAME = "pline";

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    private volatile EventAdmin eventAdmin;

    public PlineApplication() {
        super(APP_NAME, new ApiVersion(1));
        setDescription("a skysail application");
    }

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL)
    @Override
    public void setRepositories(Repositories repos) {
        super.setRepositories(repos);
    }

    public void unsetRepositories(Repositories repo) { // NOSONAR
        super.setRepositories(null);
    }

    @Activate
    @Override
    public void activate(ApplicationConfiguration appConfig, ComponentContext componentContext)
            throws ConfigurationException {
        super.activate(appConfig, componentContext);
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