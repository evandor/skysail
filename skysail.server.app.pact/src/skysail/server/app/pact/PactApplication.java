package skysail.server.app.pact;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import io.skysail.domain.core.Repositories;
import io.skysail.server.app.ApiVersion;
import io.skysail.server.app.ApplicationConfiguration;
import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.restlet.RouteBuilder;
import io.skysail.server.security.config.SecurityConfigBuilder;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class PactApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String LIST_ID = "lid";
    public static final String TODO_ID = "id";
    public static final String APP_NAME = "pact";

//    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
//    private volatile EventAdmin eventAdmin;

    public PactApplication() {
        super(APP_NAME, new ApiVersion(1));
        setDescription("The skysail demo application");
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
            .authorizeRequests().equalsMatcher("/Bookmarks/").permitAll().and()
            .authorizeRequests().startsWithMatcher("/unprotected").permitAll().and()
            .authorizeRequests().startsWithMatcher("").permitAll();
//            .authorizeRequests().startsWithMatcher("").authenticated();
    }

    @Override
    protected void attach() {
        super.attach();

        router.attach(new RouteBuilder("", PactResource.class));

        router.attach(new RouteBuilder("/pacts/{id}", PactResource.class));
        router.attach(new RouteBuilder("/pacts/", PostPactResource.class));
        router.attach(new RouteBuilder("/pacts/{id}/", PutPactResource.class));
        router.attach(new RouteBuilder("/pact", PactResource.class));

        router.attach(new RouteBuilder("/turn", NextCandiateResource.class));

       // router.attach(createStaticDirectory());

    }

}