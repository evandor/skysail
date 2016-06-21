package io.skysail.server.app.timetables.timetable;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.timetables.TimetableRepository;
import io.skysail.server.app.timetables.TimetablesApplication;
import io.skysail.server.app.timetables.course.Course;
import io.skysail.server.app.timetables.timetable.resources.TimetableResource;
import io.skysail.server.db.DbClassName;
import io.skysail.server.restlet.resources.ListServerResource;


public class TimetablesCoursesResource extends ListServerResource<Course> {

    private TimetablesApplication app;
	private TimetableRepository repo;

    public TimetablesCoursesResource() {
        super(TimetableResource.class);//, TimetablesTimetableResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "["+this.getClass().getSimpleName()+"]");
    }

    @Override
    protected void doInit() {
        app = (TimetablesApplication) getApplication();
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