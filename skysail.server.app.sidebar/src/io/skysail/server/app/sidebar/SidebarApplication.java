package io.skysail.server.app.sidebar;


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
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.restlet.RouteBuilder;
import io.skysail.server.security.config.SecurityConfigBuilder;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class SidebarApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String LIST_ID = "lid";
    public static final String TODO_ID = "id";
    public static final String APP_NAME = "sidebar";

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    private volatile EventAdmin eventAdmin;

    public SidebarApplication() {
        super(APP_NAME, new ApiVersion(1));
        setDescription("The skysail sidebar admin application");
    }

    @Activate
    @Override
    public void activate(ApplicationConfiguration appConfig, ComponentContext componentContext)
            throws ConfigurationException {
        super.activate(appConfig, componentContext);
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

        router.attach(new RouteBuilder("", SidebarsResource.class));

        router.attach(new RouteBuilder("/Sidebars", SidebarsResource.class));
//        router.attach(new RouteBuilder("", BookmarksResource.class));
//
//        // call http://localhost:2015/demoapp/v1/unprotected/times?media=json
//        router.attach(new RouteBuilder("/unprotected/times", UnprotectedTimesResource.class));
//        router.attach(new RouteBuilder("/unprotected/array", UnprotectedArrayResource.class));
//
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
//        //router.attach(new RouteBuilder("/Timetables/{id}/Courses/{targetId}/", PutTimetablesCourseResource.class));

    }

}
