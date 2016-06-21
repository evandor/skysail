package io.skysail.server.app.timetables;

import java.util.Arrays;
import java.util.HashSet;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.event.EventAdmin;
import org.restlet.data.LocalReference;
import org.restlet.routing.Router;
import org.restlet.service.CorsService;

import io.skysail.domain.core.Repositories;
import io.skysail.server.app.ApiVersion;
import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.timetables.course.resources.CourseResourceGen;
import io.skysail.server.app.timetables.course.resources.PutCourseResourceGen;
import io.skysail.server.app.timetables.timetable.PostTimetableToNewCourseRelationResource;
import io.skysail.server.app.timetables.timetable.TimetablesCourseResource;
import io.skysail.server.app.timetables.timetable.TimetablesCoursesResource;
import io.skysail.server.app.timetables.timetable.resources.PostTimetableResourceGen;
import io.skysail.server.app.timetables.timetable.resources.PutTimetableResourceGen;
import io.skysail.server.app.timetables.timetable.resources.TimetableResource;
import io.skysail.server.app.timetables.timetable.resources.TimetablesResource;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.restlet.RouteBuilder;
import io.skysail.server.security.config.SecurityConfigBuilder;
import io.skysail.server.utils.ClassLoaderDirectory;
import io.skysail.server.utils.CompositeClassLoader;

@Component(immediate = true)
public class TimetablesApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String LIST_ID = "lid";
    public static final String TODO_ID = "id";
    public static final String APP_NAME = "Timetables";

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    private volatile EventAdmin eventAdmin;

    public  TimetablesApplication() {
        super("Timetables", new ApiVersion(1));

        CorsService corsService = new CorsService();
        corsService.setAllowedOrigins(new HashSet(Arrays.asList("*")));
        corsService.setAllowedCredentials(true);
        getServices().add(corsService);
    }

    @Override
    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY)
    public void setRepositories(Repositories repos) {
        super.setRepositories(repos);
    }

    public void unsetRepositories(Repositories repo) {
        super.setRepositories(null);
    }

    @Override
    protected void defineSecurityConfig(SecurityConfigBuilder securityConfigBuilder) {
        securityConfigBuilder
            .authorizeRequests().startsWithMatcher("/Timetables").permitAll().and()
            .authorizeRequests().startsWithMatcher("/unprotected").permitAll().and()
            .authorizeRequests().startsWithMatcher("").authenticated();
    }


    @Override
    protected void attach() {
        super.attach();

        router.attach(new RouteBuilder("/Timetables/{id}", TimetableResource.class));
        router.attach(new RouteBuilder("/Timetables/", PostTimetableResourceGen.class));
        router.attach(new RouteBuilder("/Timetables/{id}/", PutTimetableResourceGen.class));
        router.attach(new RouteBuilder("/Timetables", TimetablesResource.class));
        router.attach(new RouteBuilder("", TimetablesResource.class));
        router.attach(new RouteBuilder("/Timetables/{id}/Courses", TimetablesCoursesResource.class));
        router.attach(new RouteBuilder("/Timetables/{id}/Courses/", PostTimetableToNewCourseRelationResource.class));
        router.attach(new RouteBuilder("/Timetables/{id}/Courses/{targetId}", TimetablesCourseResource.class));
        router.attach(new RouteBuilder("/Courses/{id}", CourseResourceGen.class));
        router.attach(new RouteBuilder("/Courses/{id}/", PutCourseResourceGen.class));

        // -- can be called like "io.skysail.server.app.SkysailApplication.createStaticDirectory()" with skysail.server 0.2.0 version --
        LocalReference localReference = LocalReference.createClapReference(LocalReference.CLAP_THREAD, "/Timetables/");

        CompositeClassLoader customCL = new CompositeClassLoader();
        customCL.addClassLoader(Thread.currentThread().getContextClassLoader());
        customCL.addClassLoader(Router.class.getClassLoader());
        customCL.addClassLoader(this.getClass().getClassLoader());

        ClassLoaderDirectory staticDirectory = new ClassLoaderDirectory(getContext(), localReference, customCL);

        router.attach(staticDirectory);

    }

    public EventAdmin getEventAdmin() {
        return eventAdmin;
    }

}