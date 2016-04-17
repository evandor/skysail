package io.skysail.server.app.demo.timetable;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.app.demo.DemoApplication;
import io.skysail.server.app.demo.timetable.course.Course;
import io.skysail.server.restlet.resources.PostRelationResource2;


public class PostTimetableToNewCourseRelationResource extends PostRelationResource2<Course> {

    private DemoApplication app;
    private TimetableRepository repo;
    private String parentId;

    public PostTimetableToNewCourseRelationResource() {
        // addToContext(ResourceContextId.LINK_TITLE, "add");
    }

    @Override
    protected void doInit() {
        app = (DemoApplication) getApplication();
        repo = (TimetableRepository) app.getRepository(Timetable.class);
        parentId = getAttribute("id");
    }

    public Course createEntityTemplate() {
        return new Course();
    }

    @Override
    public void addEntity(Course entity) {
        Timetable parent = repo.findOne(parentId);
        parent.getCourses().add(entity);
        repo.save(parent, getApplication().getApplicationModel());
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(TimetablesCoursesResource.class, PostTimetableToNewCourseRelationResource.class);
    }
}