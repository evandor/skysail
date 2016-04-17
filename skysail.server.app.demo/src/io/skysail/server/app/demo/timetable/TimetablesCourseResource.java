package io.skysail.server.app.demo.timetable;
import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.demo.timetable.course.Course;
import io.skysail.server.restlet.resources.EntityServerResource;

public class TimetablesCourseResource extends EntityServerResource<Course> {

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

    @Override
    public Course getEntity() {
        return null;
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(TimetablesCoursesResource.class, PostTimetablesCourseRelationResource.class);
    }

}