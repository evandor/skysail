package io.skysail.server.app.ref.fields;

import java.util.Arrays;

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
import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.app.ref.fields.domain.TextEntity;
import io.skysail.server.app.ref.fields.repositories.TextEntityRepository;
import io.skysail.server.db.DbService;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.security.config.SecurityConfigBuilder;
import lombok.Getter;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class FieldsDemoApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String APP_NAME = "fieldsdemo";

    @Reference
    private DbService dbService;

//    @Getter
//	private EntityWithoutTabssRepo entityWithoutTabssRepo;
//
//    @Getter
//    private TrixEditorEnt itysRepo trixEditorEntitysRepo;
//
//    @Getter
//    private NestedEntitysRepo nestedEntitysRepo;

    @Getter
    private DbRepository repository;

//    @Getter
//    private PasswordEntitysRepo passwordEntitysRepo;

    public FieldsDemoApplication() {
        super(APP_NAME, new ApiVersion(1), Arrays.asList(
                TextEntity.class));//, PasswordEntity.class, EntityWithoutTabs.class, NestedEntity.class, TrixEditorEntity.class));
        setDescription("a skysail application");
    }

    @Activate
    @Override
    public void activate(ApplicationConfiguration appConfig, ComponentContext componentContext)
            throws ConfigurationException {
        super.activate(appConfig, componentContext);
//        this.entityWithoutTabssRepo = new EntityWithoutTabssRepo(dbService);
//        this.trixEditorEntitysRepo = new TrixEditorEntitysRepo(dbService);
//        this.nestedEntitysRepo = new NestedEntitysRepo(dbService);
          addRepository(new TextEntityRepository(dbService));
//        this.passwordEntitysRepo = new PasswordEntitysRepo(dbService);
    }
    
    protected void defineSecurityConfig(SecurityConfigBuilder securityConfigBuilder) {
        securityConfigBuilder.authorizeRequests().startsWithMatcher("").anonymous();
    }


}