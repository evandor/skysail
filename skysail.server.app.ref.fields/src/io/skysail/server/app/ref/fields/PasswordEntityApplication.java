package io.skysail.server.app.ref.fields;

import java.util.Arrays;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;

import io.skysail.core.app.ApiVersion;
import io.skysail.core.app.SkysailApplication;
import io.skysail.server.app.ApplicationConfiguration;
import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.db.DbService;
import io.skysail.server.menus.MenuItemProvider;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class PasswordEntityApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String APP_NAME = "fieldsdemo";

    @Reference
    private DbService dbService;

//    @Getter
//	private EntityWithoutTabssRepo entityWithoutTabssRepo;
//
//    @Getter
//    private TrixEditorEntitysRepo trixEditorEntitysRepo;
//
//    @Getter
//    private NestedEntitysRepo nestedEntitysRepo;

//    @Getter
//    private Repository repository;

//    @Getter
//    private PasswordEntitysRepo passwordEntitysRepo;

    public PasswordEntityApplication() {
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
      //    addRepository(new PasswordEntityRepository(dbService));
//        this.passwordEntitysRepo = new PasswordEntitysRepo(dbService);
    }

}