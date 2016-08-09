package io.skysail.server.app.reference.one2one;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import io.skysail.domain.core.Repositories;
import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.reference.one2one.resources.PostMasterResource;
import io.skysail.server.app.reference.one2one.resources.PostMasterToNewDetailRelationResource;
import io.skysail.server.app.reference.one2one.resources.PutMasterResource;
import io.skysail.server.app.reference.one2one.resources.MasterResource;
import io.skysail.server.app.reference.one2one.resources.MastersResource;
import io.skysail.server.app.reference.one2one.resources.MastersDetailResource;
import io.skysail.server.app.reference.one2one.resources.MastersDetailsResource;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.restlet.RouteBuilder;
import io.skysail.server.security.config.SecurityConfigBuilder;

/**
 * The central application class extending SkysailApplication.
 *
 * It needs to implement ApplicationProvider to be considered by the skysail server.
 */
@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class One2OneApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public One2OneApplication() {
        super("one2manyApplication");
    }

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL)
    @Override
    public void setRepositories(Repositories repos) {
        super.setRepositories(repos);
    }

    public void unsetRepositories(Repositories repo) { // NOSONAR
        super.setRepositories(null);
    }

    @Override
    protected void defineSecurityConfig(SecurityConfigBuilder securityConfigBuilder) {
        securityConfigBuilder.authorizeRequests().startsWithMatcher("").permitAll();
    }

    @Override
    protected void attach() {
        super.attach();
        router.attach(new RouteBuilder("", MastersResource.class));

        router.attach(new RouteBuilder("/lists", MastersResource.class));
        router.attach(new RouteBuilder("/lists/", PostMasterResource.class));
        router.attach(new RouteBuilder("/lists/{id}", MasterResource.class));
        router.attach(new RouteBuilder("/lists/{id}/", PutMasterResource.class));

        router.attach(new RouteBuilder("/lists/{id}/todos", MastersDetailsResource.class));
        router.attach(new RouteBuilder("/lists/{id}/todos/", PostMasterToNewDetailRelationResource.class));
        router.attach(new RouteBuilder("/lists/{id}/todos/{targetId}", MastersDetailResource.class));
        //router.attach(new RouteBuilder("/lists/{id}/todos/{targetId}/", PutTodoListsTodoResource.class));

    }
}
