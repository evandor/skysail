package io.skysail.server.app.reference.one;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import io.skysail.domain.core.Repositories;
import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.reference.one.resources.PostTodoListResource;
import io.skysail.server.app.reference.one.resources.PostTodoListToNewTodoRelationResource;
import io.skysail.server.app.reference.one.resources.PutTodoListResource;
import io.skysail.server.app.reference.one.resources.TodoListResource;
import io.skysail.server.app.reference.one.resources.TodoListsResource;
import io.skysail.server.app.reference.one.resources.TodoListsTodoResource;
import io.skysail.server.app.reference.one.resources.TodoListsTodosResource;
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
        super("one2manyNoAggApplication");
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
        router.attach(new RouteBuilder("", TodoListsResource.class));

        router.attach(new RouteBuilder("/companies", TodoListsResource.class));
        router.attach(new RouteBuilder("/companies/", PostTodoListResource.class));
        router.attach(new RouteBuilder("/companies/{id}", TodoListResource.class));
        router.attach(new RouteBuilder("/companies/{id}/", PutTodoListResource.class));

        router.attach(new RouteBuilder("/companies/{id}/contacts", TodoListsTodosResource.class));
        router.attach(new RouteBuilder("/companies/{id}/contacts/", PostTodoListToNewTodoRelationResource.class));
        router.attach(new RouteBuilder("/companies/{id}/contacts/{targetId}", TodoListsTodoResource.class));
        //router.attach(new RouteBuilder("/lists/{id}/todos/{targetId}/", PutTodoListsTodoResource.class));

    }
}
