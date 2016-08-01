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
    private Repository repository;

    public PostCourseResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new ");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (DemoApplication) getApplication();
        repository = app.getRepository(Timetable.class);
    }

    @Override
    public Course createEntityTemplate() {
        return new Course();
    }

    @Override
    public void addEntity(Course entity) {
//        Subject subject = SecurityUtils.getSubject();

        Timetable entityRoot = (Timetable) repository.findOne(getAttribute("id"));
        entityRoot.getCourses().add(entity);
        repository.update(entityRoot, app.getApplicationModel());
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(CoursesResource.class);
    }
}