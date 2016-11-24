package io.skysail.server.app.demo.timetable.course.resources;

import org.restlet.resource.ResourceException;

import io.skysail.server.app.demo.DemoApplication;
import io.skysail.server.app.demo.timetable.course.Course;
import io.skysail.server.app.demo.timetable.timetables.Timetable;
import io.skysail.server.restlet.resources.PutEntityServerResource;

public class PutCourseResource extends PutEntityServerResource<Course> {


    protected String id;
    protected DemoApplication app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (DemoApplication)getApplication();
    }

    @Override
    public void updateEntity(Course  entity) {
       Course original = getEntity();
        copyProperties(original,entity);

        app.getTtRepo().update(original,app.getApplicationModel());
    }

    @Override
    public Course getEntity() {
        return null;// (Course)app.getTtRepo().findOne(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(CoursesResource.class);
    }
}