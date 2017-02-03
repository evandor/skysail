package io.skysail.product.designer;

import org.osgi.service.component.annotations.Component;

import io.skysail.server.app.ApiVersion;
import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.RouteBuilder;
import io.skysail.server.security.config.SecurityConfigBuilder;

@Component(immediate = true)
public class DesignerRootApplication extends SkysailApplication implements ApplicationProvider {

    public DesignerRootApplication() {
        super("designer", new ApiVersion(1));
    }
    
    @Override
    protected void defineSecurityConfig(SecurityConfigBuilder securityConfigBuilder) {
    	securityConfigBuilder.authorizeRequests().startsWithMatcher("").permitAll();
    }
    
    @Override
    protected void attach() {
        //router.setApiVersion(null);
        router.attach(new RouteBuilder("", PublicResource.class));
    }
}
