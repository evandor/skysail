package io.skysail.server.app.demo;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.event.EventAdmin;

import io.skysail.core.app.ApiVersion;
import io.skysail.core.app.SkysailApplication;
import io.skysail.server.app.ApplicationConfiguration;
import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.demo.resources.BookmarkResource;
import io.skysail.server.app.demo.resources.BookmarksResource;
import io.skysail.server.app.demo.resources.PostBookmarkResource;
import io.skysail.server.app.demo.resources.PutBookmarkResource;
import io.skysail.server.app.demo.timetable.repo.TimetableRepository;
import io.skysail.server.db.DbService;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.restlet.RouteBuilder;
import io.skysail.server.security.config.SecurityConfigBuilder;
import lombok.Getter;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class DemoApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String LIST_ID = "lid";
    public static final String TODO_ID = "id";
    public static final String APP_NAME = "demoapp";

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    private volatile EventAdmin eventAdmin;

    @Reference
    private DbService dbService;

    @Getter
    private DemoRepository repo;

    @Getter
    private TimetableRepository ttRepo;

    public DemoApplication() {
        super("demoapp", new ApiVersion(1));
        setDescription("The skysail demo application");
    }

    @Activate
    @Override
    public void activate(ApplicationConfiguration appConfig, ComponentContext componentContext)
            throws ConfigurationException {
        super.activate(appConfig, componentContext);
        repo = new DemoRepository(dbService);
        ttRepo = new TimetableRepository(dbService);
    }

    @Override
    protected void defineSecurityConfig(SecurityConfigBuilder securityConfigBuilder) {
        securityConfigBuilder
            .authorizeRequests().startsWithMatcher("/mailgun").permitAll().and()
            .authorizeRequests().equalsMatcher("/Bookmarks/").permitAll().and()
            .authorizeRequests().startsWithMatcher("/unprotected").permitAll().and()
            .authorizeRequests().startsWithMatcher("").authenticated();
    }

    @Override
    protected void attach() {
        super.attach();

        router.attach(new RouteBuilder("/Bookmarks/{id}", BookmarkResource.class));
        router.attach(new RouteBuilder("/Bookmarks/", PostBookmarkResource.class));
        router.attach(new RouteBuilder("/Bookmarks/{id}/", PutBookmarkResource.class));
        router.attach(new RouteBuilder("/Bookmarks", BookmarksResource.class));
        router.attach(new RouteBuilder("", BookmarksResource.class));

        // call http://localhost:2015/demoapp/v1/unprotected/times?media=json
        router.attach(new RouteBuilder("/unprotected/times", UnprotectedTimesResource.class));
        router.attach(new RouteBuilder("/unprotected/array", UnprotectedArrayResource.class));

//        router.attach(new RouteBuilder("", TimetablesResource.class));
//
//        router.attach(new RouteBuilder("/Timetables/{id}", TimetableResource.class));
//        router.attach(new RouteBuilder("/Timetables/", PostTimetableResource.class));
//        router.attach(new RouteBuilder("/Timetables/{id}/", PutTimetableResource.class));
//        router.attach(new RouteBuilder("/Timetables", TimetablesResource.class));
//
//        router.attach(new RouteBuilder("/Timetables/{id}/Courses", TimetablesCoursesResource.class));
//        router.attach(new RouteBuilder("/Timetables/{id}/Courses/", PostTimetableToNewCourseRelationResource.class));
//        router.attach(new RouteBuilder("/Timetables/{id}/Courses/{targetId}", TimetablesCourseResource.class));
//        router.attach(new RouteBuilder("/Timetables/{id}/Courses/{targetId}/", PutTimetablesCourseResource.class));
//
//
//        router.attach(new RouteBuilder("/Timetables/{id}/Courses/{courseId}/notifications", CoursesNotificationsResource.class));
//        router.attach(new RouteBuilder("/Timetables/{id}/Courses/{courseId}/notifications/", PostCourseToNewNotificationRelationResource.class));
//        router.attach(new RouteBuilder("/Timetables/{id}/Courses/{courseId}/notifications/{targetId}", CoursesNotificationResource.class));
        //router.attach(new RouteBuilder("/Timetables/{id}/Courses/{targetId}/", PutTimetablesCourseResource.class));



//        router.attach(new RouteBuilder("/Courses/{id}", CourseResourceGen.class));
//        router.attach(new RouteBuilder("/Courses/{id}/", PutCourseResourceGen.class));

        router.attach(new RouteBuilder("/mailgun", MailgunResource.class));

        //router.attach(createStaticDirectory());

    }

}