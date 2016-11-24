package io.skysail.server.app.demo.timetable.course.resources;

import org.restlet.resource.ResourceException;

import io.skysail.domain.core.repos.Repository;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.demo.DemoApplication;
import io.skysail.server.app.demo.timetable.course.Course;
import io.skysail.server.app.demo.timetable.timetables.Timetable;
import io.skysail.server.restlet.resources.PostEntityServerResource;

public class PostCourseResource extends PostEntityServerResource<Course> {

	private DemoApplication app;

    public PostCourseResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new ");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (DemoApplication) getApplication();
    }

    @Override
    public Course createEntityTemplate() {
        return new Course();
    }

    @Override
    public void addEntity(Course entity) {
//        Subject subject = SecurityUtils.getSubject();

        Timetable entityRoot = app.getTtRepo().findOne(getAttribute("id"));
        entityRoot.getCourses().add(entity);
        app.getTtRepo().update(entityRoot, app.getApplicationModel());
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(CoursesResource.class);
    }
}