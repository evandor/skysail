package io.skysail.server.app.ref.singleentity;

import org.osgi.service.component.annotations.Component;
import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.ref.singleentity.resources.HtmlResource;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.restlet.RouteBuilder;
import io.skysail.server.security.config.SecurityConfigBuilder;

/**
 * The central application class extending SkysailApplication.
 *
 * It needs to implement ApplicationProvider to be considered by the skysail server.
 */
@Component(immediate = true)
public class HtmlProxyApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public HtmlProxyApplication() {
        super("htmlProxyApplication");
    }

    /**
     * all access to routes is restricted to authenticated users.
     */
    @Override
    protected void defineSecurityConfig(SecurityConfigBuilder securityConfigBuilder) {
        securityConfigBuilder.authorizeRequests().startsWithMatcher("").permitAll();
    }

    @Override
    protected void attach() {
        super.attach();
        router.attach(new RouteBuilder("/url/{id}", HtmlResource.class));
    }
}
