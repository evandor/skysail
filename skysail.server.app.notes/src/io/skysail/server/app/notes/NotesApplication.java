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
import io.skysail.server.app.notes.repos.AwsNotesRepository;
import io.skysail.server.app.notes.repos.NotesRepository;
import io.skysail.server.app.notes.resources.CategoriesResource;
import io.skysail.server.app.notes.resources.NoteResource;
import io.skysail.server.app.notes.resources.NotesClientResource;
import io.skysail.server.app.notes.resources.NotesResource;
import io.skysail.server.app.notes.resources.PostNoteResource;
import io.skysail.server.app.notes.resources.PutNoteResource;
import io.skysail.server.app.notes.resources.SyncRequestResource;
import io.skysail.server.app.notes.resources.UpdateRequestResource;
import io.skysail.server.app.notes.service.AwsSyncService;
import io.skysail.server.db.DbService;
import io.skysail.server.executors.SkysailExecutorService;
import io.skysail.server.ext.aws.AwsConfiguration;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.restlet.RouteBuilder;
import io.skysail.server.security.config.SecurityConfigBuilder;
import lombok.Getter;

/**
 * A simple "notes" application for a single user (no shared notes).
 *
 * The data is backed up by AWS, in a table "notes", if AWS credentials are provided via aws.cfg.
 * In aws.cfg you define the profile name and region to be used; the profile name must match an entry
 * in your <user>/.aws/credentials file.
 *
 * The notes application will keep note items in sync with the AWS backend; if you run it on a different
 * machine with the same aws account set up, the notes will sync automatically.
 *
 */
@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class NotesApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String APP_NAME = "notes";

    @Reference
    private DbService dbService;

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    private AwsConfiguration awsConfig;

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    private SkysailExecutorService executor;

    @Getter
    private NotesRepository repo;

    @Getter
    private AwsNotesRepository eventRepo;

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    private volatile EventAdmin eventAdmin;

    @Getter
    private AwsSyncService syncService;

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
        this.eventRepo = new AwsNotesRepository(awsConfig, executor, eventAdmin);
        this.syncService = new AwsSyncService(this);
        //pullFromAwsRepo();
    }

//    private void pullFromAwsRepo() {
//        awsRepo.findAll().stream()
//            .forEach(noteFromAws -> {
//                List<Note> found = this.repo.find(new Filter("(uuid="+noteFromAws.getUuid()+")"));
//                if (found == null || found.isEmpty()) {
//                    Note note = new Note();
//                    note.setContent(noteFromAws.getContent());
//                    note.setTitle(noteFromAws.getTitle());
//                    note.setUuid(noteFromAws.getUuid());
//                    this.repo.save(note, getApplicationModel());
//                }
//            });
//    }

    @Override
    protected void defineSecurityConfig(SecurityConfigBuilder securityConfigBuilder) {
        securityConfigBuilder
            .authorizeRequests()
                .startsWithMatcher("/sync").permitAll()
                .startsWithMatcher("").authenticated();
    }

    @Override
    protected void attach() {
        super.attach();

        router.attach(new RouteBuilder("/notes/{id}", NoteResource.class));
        router.attach(new RouteBuilder("/notes/", PostNoteResource.class));
        router.attach(new RouteBuilder("/notes/{id}/", PutNoteResource.class));
        router.attach(new RouteBuilder("/notes", NotesResource.class));

        router.attach(new RouteBuilder("/categories", CategoriesResource.class));

        router.attach(new RouteBuilder("/updates", UpdateRequestResource.class));

        router.attach(new RouteBuilder("", NotesClientResource.class));

        router.attach(new RouteBuilder("/sync", SyncRequestResource.class));

    }

}