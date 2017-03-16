package io.skysail.server.app.ref.fields;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;

import io.skysail.core.app.ApiVersion;
import io.skysail.core.app.ApplicationConfiguration;
import io.skysail.core.app.ApplicationProvider;
import io.skysail.core.app.SkysailApplication;
import io.skysail.server.app.ref.fields.domain.PostTextEntityResource;
import io.skysail.server.app.ref.fields.domain.PutTextEntityResource;
import io.skysail.server.app.ref.fields.domain.TextEntityResource;
import io.skysail.server.app.ref.fields.domain.TextEntitysResource;
import io.skysail.server.app.ref.fields.repositories.TextEntityRepository;
import io.skysail.server.db.DbService;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.restlet.RouteBuilder;
import io.skysail.server.security.config.SecurityConfigBuilder;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class FieldsDemoApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String APP_NAME = "fieldsdemo";

    @Reference
    private DbService dbService;

    public FieldsDemoApplication() {
        super(APP_NAME, new ApiVersion(1));
        setDescription("skysail demo application for fields");
    }

    @Activate
    @Override
    public void activate(ApplicationConfiguration appConfig, ComponentContext componentContext)
            throws ConfigurationException {
        super.activate(appConfig, componentContext);
          addRepository(new TextEntityRepository(dbService));
         // addRepository(new PasswordEntityRepository(dbService));
    }

    @Override
    protected void defineSecurityConfig(SecurityConfigBuilder securityConfigBuilder) {
        securityConfigBuilder.authorizeRequests().startsWithMatcher("").anonymous();
    }

    @Override
    protected void attach() {
    	super.attach();

//    	router.attach(new RouteBuilder("", PasswordEntitysResource.class));

    	router.attach(new RouteBuilder("/texts", TextEntitysResource.class));
    	router.attach(new RouteBuilder("/texts/", PostTextEntityResource.class));
    	router.attach(new RouteBuilder("/texts/{id}", TextEntityResource.class));
    	router.attach(new RouteBuilder("/texts/{id}/", PutTextEntityResource.class));

//    	router.attach(new RouteBuilder("/passwords", PasswordEntitysResource.class));
//    	router.attach(new RouteBuilder("/passwords/", PostPasswordEntityResource.class));
//    	router.attach(new RouteBuilder("/passwords/{id}", PasswordEntityResource.class));
//    	router.attach(new RouteBuilder("/passwords/{id}/", PutPasswordEntityResource.class));
    }


}