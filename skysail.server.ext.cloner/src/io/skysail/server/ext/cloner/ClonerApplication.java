package io.skysail.server.ext.cloner;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.restlet.Restlet;
import org.restlet.resource.Directory;
import org.restlet.routing.Router;

import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.ext.cloner.resources.InstallationCloningResource;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.restlet.RouteBuilder;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class ClonerApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    private ClonerConfigProvider clonerConfigProvider;

    public ClonerApplication() {
        // super("cloner", new ApiVersion(1));
        setName("cloner");
        setDescription("The skysail server cloning application");
    }

    // @Override
    // protected void defineSecurityConfig(SecurityConfigBuilder
    // securityConfigBuilder) {
    // securityConfigBuilder.authorizeRequests().startsWithMatcher("/unprotected").permitAll().and()
    // .authorizeRequests().startsWithMatcher("").authenticated();
    // }

    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(getContext());
        String rootUri = clonerConfigProvider.getBaseDir();
        Directory directory = new Directory(getContext(), rootUri);
        directory.setListingAllowed(true);
        router.attach("/home", directory);
        // router.attach("/hello", HelloServerResource.class);
        return router;
    }

    @Override
    protected void attach() {
        super.attach();

        router.attach(new RouteBuilder("/installation", InstallationCloningResource.class));
        // router.attach(createStaticDirectory());
    }

}
