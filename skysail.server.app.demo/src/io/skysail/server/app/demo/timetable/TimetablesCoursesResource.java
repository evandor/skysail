package io.skysail.server.app.demo.timetable;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.demo.DemoApplication;
import io.skysail.server.app.demo.timetable.course.Course;
import io.skysail.server.app.demo.timetable.resources.TimetableResource;
import io.skysail.server.db.DbClassName;
import io.skysail.server.restlet.resources.ListServerResource;


public class TimetablesCoursesResource extends ListServerResource<Course> {

    private DemoApplication app;
	private TimetableRepository repo;

    public TimetablesCoursesResource() {
        super(TimetableResource.class);//, TimetablesTimetableResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "["+this.getClass().getSimpleName()+"]");
    }

    @Override
    protected void doInit() {
        app = (DemoApplication) getApplication();
        repo = (TimetableRepository) app.getRepository(Timetable.class);
    }

    @Override
    public List<Course> getEntity() {
        return (List<Course>) repo.execute(Course.class, "select * from " + DbClassName.of(Course.class) + " where #"+getAttribute("id")+" in IN(courses)");
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(TimetablesCoursesResource.class, PostTimetablesCourseRelationResource.class);
    }
}