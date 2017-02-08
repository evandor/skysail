package io.skysail.server.app.ref.one2one;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;

import io.skysail.core.app.SkysailApplication;
import io.skysail.domain.Entity;
import io.skysail.domain.core.repos.Repository;
import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.ref.one2one.resources.MasterResource;
import io.skysail.server.app.ref.one2one.resources.MastersDetailResource;
import io.skysail.server.app.ref.one2one.resources.MastersDetailsResource;
import io.skysail.server.app.ref.one2one.resources.MastersResource;
import io.skysail.server.app.ref.one2one.resources.PostMasterResource;
import io.skysail.server.app.ref.one2one.resources.PostMasterToNewDetailRelationResource;
import io.skysail.server.app.ref.one2one.resources.PutMasterResource;
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
@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class One2OneApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    @Reference
    private DbService dbService;

    @Getter
    private One2OneRepository repo;

    public One2OneApplication() {
        super("one2manyApplication");
        repo = new One2OneRepository(dbService);
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
