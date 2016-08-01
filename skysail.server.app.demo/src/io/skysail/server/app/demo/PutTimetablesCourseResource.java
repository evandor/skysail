package io.skysail.server.app.demo;

import org.restlet.resource.ResourceException;

import io.skysail.server.ResourceContextId;
import io.skysail.server.app.demo.timetable.course.Course;
import io.skysail.server.app.demo.timetable.timetables.Timetable;
import io.skysail.server.app.demo.timetable.timetables.resources.TimetableResource;
import io.skysail.server.restlet.resources.PutEntityServerResource;

public class PutTimetablesCourseResource extends PutEntityServerResource<Course> {

    private DemoApplication app;
    private Timetable timetable;

    public PutTimetablesCourseResource() {
        addToContext(ResourceContextId.LINK_TITLE, "update course");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (DemoApplication) getApplication();
        timetable = (Timetable) app.getRepository(Timetable.class).findOne(getAttribute("id"));
    }

    @Override
    public void updateEntity(Course entity) {
        Course original = getEntity();
        copyProperties(original, entity);
        timetable.updateCourse(original);
        app.getRepository(Timetable.class).update(timetable, app.getApplicationModel());
    }

    @Override
    public Course getEntity() {
        return timetable.getCourse(getAttribute("targetId"));
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(TimetableResource.class);
    }

}
