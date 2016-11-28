package io.skysail.server.app.demo.timetable.course.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.demo.DemoApplication;
import io.skysail.server.app.demo.timetable.course.Course;
import io.skysail.server.app.demo.timetable.repo.TimetableRepository;
import io.skysail.server.app.demo.timetable.timetables.Timetable;
import io.skysail.server.restlet.resources.PostRelationResource2;


public class PostTimetableToNewCourseRelationResource extends PostRelationResource2<Course> {

    private DemoApplication app;
    private String parentId;

    public PostTimetableToNewCourseRelationResource() {
        addToContext(ResourceContextId.LINK_TITLE, "create new course for this timetable");
    }

    @Override
    protected void doInit() {
        app = (DemoApplication) getApplication();
        parentId = getAttribute("id");
    }

    @Override
    public Course createEntityTemplate() {
        return new Course();
    }

    @Override
    public void addEntity(Course entity) {
        Timetable parent = app.getTtRepo().findOne(parentId);
        parent.getCourses().add(entity);
        app.getTtRepo().save(parent, getApplication().getApplicationModel());
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(TimetablesCoursesResource.class, PostTimetableToNewCourseRelationResource.class);
    }
}