package io.skysail.server.app.reference.one2many;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import io.skysail.domain.core.Repositories;
import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.reference.one2many.resources.PostTodoListResource;
import io.skysail.server.app.reference.one2many.resources.PutTodoListResource;
import io.skysail.server.app.reference.one2many.resources.TodoListResource;
import io.skysail.server.app.reference.one2many.resources.TodoListsResource;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.restlet.RouteBuilder;
import io.skysail.server.security.config.SecurityConfigBuilder;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class One2ManyApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public One2ManyApplication() {
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
        router.attach(new RouteBuilder("", TodoListsResource.class));
        router.attach(new RouteBuilder("/accounts", TodoListsResource.class));
        router.attach(new RouteBuilder("/accounts/", PostTodoListResource.class));
        router.attach(new RouteBuilder("/accounts/{id}", TodoListResource.class));
        router.attach(new RouteBuilder("/accounts/{id}/", PutTodoListResource.class));
    }
}
