package io.skysail.server.app.demo;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.event.EventAdmin;

import io.skysail.domain.core.Repositories;
import io.skysail.server.app.ApiVersion;
import io.skysail.server.app.ApplicationConfiguration;
import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.demo.resources.BookmarkResource;
import io.skysail.server.app.demo.resources.BookmarksResource;
import io.skysail.server.app.demo.resources.PostBookmarkResource;
import io.skysail.server.app.demo.resources.PutBookmarkResource;
import io.skysail.server.app.demo.timetable.PostTimetableToNewCourseRelationResource;
import io.skysail.server.app.demo.timetable.TimetablesCourseResource;
import io.skysail.server.app.demo.timetable.TimetablesCoursesResource;
import io.skysail.server.app.demo.timetable.course.resources.CourseResourceGen;
import io.skysail.server.app.demo.timetable.course.resources.PutCourseResourceGen;
import io.skysail.server.app.demo.timetable.resources.PostTimetableResource;
import io.skysail.server.app.demo.timetable.resources.PutTimetableResource;
import io.skysail.server.app.demo.timetable.resources.TimetableResource;
import io.skysail.server.app.demo.timetable.resources.TimetablesResource;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.restlet.RouteBuilder;
import io.skysail.server.security.config.SecurityConfigBuilder;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class DemoApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String LIST_ID = "lid";
    public static final String TODO_ID = "id";
    public static final String APP_NAME = "demoapp";

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    private volatile EventAdmin eventAdmin;
    
    public DemoApplication() {
        super("demoapp", new ApiVersion(1));
        setDescription("The skysail demo application");
    }

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL)
    @Override
    public void setRepositories(Repositories repos) {
        super.setRepositories(repos);
    }

    public void unsetRepositories(Repositories repo) { // NOSONAR
        super.setRepositories(null);
    }
    
    @Activate
    @Override
    public void activate(ApplicationConfiguration appConfig, ComponentContext componentContext) throws ConfigurationException {
    	super.activate(appConfig, componentContext);
    }
    
    @Override
    protected void defineSecurityConfig(SecurityConfigBuilder securityConfigBuilder) {
    	securityConfigBuilder
//    	.exceptionHandling()
//        .authenticationEntryPoint(spnegoEntryPoint())
//        .and()
    		.authorizeRequests()
    			.startsWithMatcher("").authenticated();
//    			.antMatchers("7/").anonymous()
//    			.anyRequest().authenticated()
//    			.and()
//    		.httpBasic();
    		;
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
        router.attach(new RouteBuilder("/unprotected/times", UnprotectedTimesResource.class).noAuthenticationNeeded());
        router.attach(new RouteBuilder("/unprotected/array", UnprotectedArrayResource.class).noAuthenticationNeeded());
        
        router.attach(new RouteBuilder("/Timetables/{id}", TimetableResource.class));
        router.attach(new RouteBuilder("/Timetables/", PostTimetableResource.class));
        router.attach(new RouteBuilder("/Timetables/{id}/", PutTimetableResource.class));
        router.attach(new RouteBuilder("/Timetables", TimetablesResource.class));
        router.attach(new RouteBuilder("", TimetablesResource.class));
        router.attach(new RouteBuilder("/Timetables/{id}/Courses", TimetablesCoursesResource.class));
        router.attach(new RouteBuilder("/Timetables/{id}/Courses/", PostTimetableToNewCourseRelationResource.class));
        router.attach(new RouteBuilder("/Timetables/{id}/Courses/{targetId}", TimetablesCourseResource.class));
        router.attach(new RouteBuilder("/Courses/{id}", CourseResourceGen.class));
        router.attach(new RouteBuilder("/Courses/{id}/", PutCourseResourceGen.class));

        router.attach(createStaticDirectory());     
                
    }

    @Override
	public EventAdmin getEventAdmin() {
        return eventAdmin;
    }
	
}