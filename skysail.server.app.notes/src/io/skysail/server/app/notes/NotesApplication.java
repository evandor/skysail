package io.skysail.server.app.notes;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.event.EventAdmin;

import io.skysail.server.app.ApiVersion;
import io.skysail.server.app.ApplicationConfiguration;
import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.notes.resources.NoteResource;
import io.skysail.server.app.notes.resources.NotesResource;
import io.skysail.server.app.notes.resources.PostNoteResource;
import io.skysail.server.app.notes.resources.PutNoteResource;
import io.skysail.server.db.DbService;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.restlet.RouteBuilder;
import io.skysail.server.security.config.SecurityConfigBuilder;
import lombok.Getter;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class NotesApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String APP_NAME = "notes";

    @Reference
    private DbService dbService;

    @Getter
    private NotesRepository repo;

    @Getter
    private DDBNotesRepository awsRepo;

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    private volatile EventAdmin eventAdmin;

    public NotesApplication() {
        super(APP_NAME, new ApiVersion(1));
        setDescription("a skysail application");
    }

    @Activate
    @Override
    public void activate(ApplicationConfiguration appConfig, ComponentContext componentContext)
            throws ConfigurationException {
        super.activate(appConfig, componentContext);
        this.repo = new NotesRepository(dbService);
        this.awsRepo = new DDBNotesRepository();
    }

    @Override
    protected void defineSecurityConfig(SecurityConfigBuilder securityConfigBuilder) {
        securityConfigBuilder
            .authorizeRequests().startsWithMatcher("").authenticated();
    }

    @Override
    protected void attach() {
        super.attach();

        router.attach(new RouteBuilder("/Bookmarks/{id}", NoteResource.class));
        router.attach(new RouteBuilder("/Bookmarks/", PostNoteResource.class));
        router.attach(new RouteBuilder("/Bookmarks/{id}/", PutNoteResource.class));
        router.attach(new RouteBuilder("/Bookmarks", NotesResource.class));
        router.attach(new RouteBuilder("", NotesResource.class));
    }

}