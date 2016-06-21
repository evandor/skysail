package io.skysail.server.app.timetables.course.resources;

import org.restlet.resource.ResourceException;

import io.skysail.domain.core.repos.Repository;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.timetables.TimetablesApplication;
import io.skysail.server.app.timetables.course.Course;
import io.skysail.server.app.timetables.timetable.Timetable;
import io.skysail.server.restlet.resources.PostEntityServerResource;

public class PostCourseResource extends PostEntityServerResource<io.skysail.server.app.timetables.course.Course> {

	private TimetablesApplication app;
    private Repository repository;

    public PostCourseResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new ");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (TimetablesApplication) getApplication();
        repository = app.getRepository(Timetable.class);
    }

    @Override
    public io.skysail.server.app.timetables.course.Course createEntityTemplate() {
        return new Course();
    }

    @Override
    public void addEntity(io.skysail.server.app.timetables.course.Course entity) {
        //Subject subject = SecurityUtils.getSubject();

        Timetable entityRoot = (Timetable) repository.findOne(getAttribute("id"));
        entityRoot.getCourses().add(entity);
        repository.update(entityRoot, app.getApplicationModel());
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(CoursesResourceGen.class);
    }
}