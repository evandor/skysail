package io.skysail.server.app.ref.one2many.noagg;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;

import io.skysail.domain.Entity;
import io.skysail.domain.core.repos.Repository;
import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.ref.one2many.noagg.resources.CompaniesResource;
import io.skysail.server.app.ref.one2many.noagg.resources.CompanyResource;
import io.skysail.server.app.ref.one2many.noagg.resources.CompanysContactResource;
import io.skysail.server.app.ref.one2many.noagg.resources.CompanysContactsResource;
import io.skysail.server.app.ref.one2many.noagg.resources.PostCompanyResource;
import io.skysail.server.app.ref.one2many.noagg.resources.PostTodoListToNewTodoRelationResource;
import io.skysail.server.app.ref.one2many.noagg.resources.PutCompanyResource;
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
public class One2ManyNoAggApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    @Reference
    private DbService dbService;

    @Getter
    private One2ManyNoAggRepository repo;

    public One2ManyNoAggApplication() {
        super("one2manyApplicationNoAgg");
        repo = new One2ManyNoAggRepository(dbService);
    }

    @Override
    protected void defineSecurityConfig(SecurityConfigBuilder securityConfigBuilder) {
        securityConfigBuilder.authorizeRequests().startsWithMatcher("").permitAll();
    }

    @Override
    protected void attach() {
        super.attach();

        router.attach(new RouteBuilder("/lists", CompaniesResource.class));
        router.attach(new RouteBuilder("/lists/", PostCompanyResource.class));
        router.attach(new RouteBuilder("/lists/{id}", CompanyResource.class));
        router.attach(new RouteBuilder("/lists/{id}/", PutCompanyResource.class));

        router.attach(new RouteBuilder("/lists/{id}/todos", CompanysContactsResource.class));
        router.attach(new RouteBuilder("/lists/{id}/todos/", PostTodoListToNewTodoRelationResource.class));
        router.attach(new RouteBuilder("/lists/{id}/todos/{targetId}", CompanysContactResource.class));
        //router.attach(new RouteBuilder("/lists/{id}/todos/{targetId}/", PutTodoListsTodoResource.class));

        router.attachDefaultRoot();
    }
}
