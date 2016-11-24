package io.skysail.server.app.demo.timetable.course.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.demo.DemoApplication;
import io.skysail.server.app.demo.timetable.course.Course;
import io.skysail.server.app.demo.timetable.repo.TimetableRepository;
import io.skysail.server.app.demo.timetable.timetables.Timetable;
import io.skysail.server.db.DbClassName;
import io.skysail.server.restlet.resources.ListServerResource;


public class TimetablesCoursesResource extends ListServerResource<Course> {

    private DemoApplication app;

    public TimetablesCoursesResource() {
        super(TimetablesCourseResource.class);
        addToContext(ResourceContextId.LINK_GLYPH, "list");
        addToContext(ResourceContextId.LINK_TITLE, "list courses for this timetable");
    }

    @Override
    protected void doInit() {
        app = (DemoApplication) getApplication();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Course> getEntity() {
        String sql = "select * from " + DbClassName.of(Course.class) + " where #"+getAttribute("id")+" in IN(courses)";
        return (List<Course>) app.getTtRepo().execute(Course.class, sql);
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(TimetablesCoursesResource.class, PostTimetableToNewCourseRelationResource.class);
    }
}