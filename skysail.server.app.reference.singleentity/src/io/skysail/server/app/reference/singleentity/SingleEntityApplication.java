package io.skysail.server.app.reference.singleentity;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import io.skysail.domain.core.Repositories;
import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.reference.singleentity.resources.AccountResource;
import io.skysail.server.app.reference.singleentity.resources.AccountsResource;
import io.skysail.server.app.reference.singleentity.resources.PostAccountResource;
import io.skysail.server.app.reference.singleentity.resources.PutAccountResource;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.restlet.RouteBuilder;
import io.skysail.server.security.config.SecurityConfigBuilder;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class SingleEntityApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public SingleEntityApplication() {
        super("singleEntityApplication");
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
        router.attach(new RouteBuilder("", AccountsResource.class));
        router.attach(new RouteBuilder("/accounts", AccountsResource.class));
        router.attach(new RouteBuilder("/accounts/", PostAccountResource.class));
        router.attach(new RouteBuilder("/accounts/{id}", AccountResource.class));
        router.attach(new RouteBuilder("/accounts/{id}/", PutAccountResource.class));
    }
}