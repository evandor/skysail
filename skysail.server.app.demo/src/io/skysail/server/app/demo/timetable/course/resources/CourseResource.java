package io.skysail.server.app.demo.timetable.course.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.demo.DemoApplication;
import io.skysail.server.app.demo.timetable.course.Course;
import io.skysail.server.restlet.resources.EntityServerResource;


public class CourseResource extends EntityServerResource<Course> {

    private String id;
    private DemoApplication app;

    public CourseResource() {
        addToContext(ResourceContextId.LINK_TITLE, "details");
        addToContext(ResourceContextId.LINK_GLYPH, "search");
    }

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (DemoApplication) getApplication();
       // repository = (CourseRepository) app.getRepository(io.skysail.server.app.timetables.course.Course.class);
    }

    @Override
    public Course getEntity() {
        return (Course)app.getRepository().findOne(id);
    }

	@Override
    public List<Link> getLinks() {
        return super.getLinks(PutCourseResource.class);
    }

}