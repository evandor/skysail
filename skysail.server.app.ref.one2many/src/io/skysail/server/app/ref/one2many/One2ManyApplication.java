package io.skysail.server.app.ref.one2many;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;

import io.skysail.domain.Identifiable;
import io.skysail.domain.core.repos.Repository;
import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.ref.one2many.resources.PostTodoListResource;
import io.skysail.server.app.ref.one2many.resources.PostTodoListToNewTodoRelationResource;
import io.skysail.server.app.ref.one2many.resources.PutTodoListResource;
import io.skysail.server.app.ref.one2many.resources.TodoListResource;
import io.skysail.server.app.ref.one2many.resources.TodoListsResource;
import io.skysail.server.app.ref.one2many.resources.TodoListsTodoResource;
import io.skysail.server.app.ref.one2many.resources.TodoListsTodosResource;
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
public class One2ManyApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    @Reference
    private DbService dbService;

    @Getter
    private One2ManyRepository repo;

    public One2ManyApplication() {
        super("one2manyApplication");
        repo = new One2ManyRepository(dbService);
    }

    @Override
    protected void defineSecurityConfig(SecurityConfigBuilder securityConfigBuilder) {
        securityConfigBuilder.authorizeRequests().startsWithMatcher("").permitAll();
    }

    @Override
    protected void attach() {
        super.attach();

        router.attach(new RouteBuilder("", TodoListsResource.class));

        router.attach(new RouteBuilder("/lists", TodoListsResource.class));
        router.attach(new RouteBuilder("/lists/", PostTodoListResource.class));
        router.attach(new RouteBuilder("/lists/{id}", TodoListResource.class));
        router.attach(new RouteBuilder("/lists/{id}/", PutTodoListResource.class));

        router.attach(new RouteBuilder("/lists/{id}/todos", TodoListsTodosResource.class));
        router.attach(new RouteBuilder("/lists/{id}/todos/", PostTodoListToNewTodoRelationResource.class));
        router.attach(new RouteBuilder("/lists/{id}/todos/{targetId}", TodoListsTodoResource.class));
        //router.attach(new RouteBuilder("/lists/{id}/todos/{targetId}/", PutTodoListsTodoResource.class));

    }
}
