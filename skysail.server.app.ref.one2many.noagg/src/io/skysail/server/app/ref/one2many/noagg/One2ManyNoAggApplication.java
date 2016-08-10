package io.skysail.server.app.ref.one2many.noagg;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import io.skysail.domain.core.Repositories;
import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.ref.one2many.noagg.resources.CompaniesResource;
import io.skysail.server.app.ref.one2many.noagg.resources.CompanyResource;
import io.skysail.server.app.ref.one2many.noagg.resources.PostCompanyResource;
import io.skysail.server.app.ref.one2many.noagg.resources.PostTodoListToNewTodoRelationResource;
import io.skysail.server.app.ref.one2many.noagg.resources.PutCompanyResource;
import io.skysail.server.app.ref.one2many.noagg.resources.CompanysContactResource;
import io.skysail.server.app.ref.one2many.noagg.resources.CompanysContactsResource;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.restlet.RouteBuilder;
import io.skysail.server.security.config.SecurityConfigBuilder;

/**
 * The central application class extending SkysailApplication.
 *
 * It needs to implement ApplicationProvider to be considered by the skysail server.
 */
@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class One2ManyNoAggApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public One2ManyNoAggApplication() {
        super("one2manyApplicationNoAgg");
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
