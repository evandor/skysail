package io.skysail.server.app.timetables.timetable.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ResourceContextId;
import io.skysail.server.restlet.resources.EntityServerResource;
import io.skysail.server.app.timetables.*;

import io.skysail.server.app.timetables.timetable.*;
import io.skysail.server.app.timetables.timetable.resources.*;
import io.skysail.server.app.timetables.course.*;
import io.skysail.server.app.timetables.course.resources.*;


public class TimetableResource extends EntityServerResource<io.skysail.server.app.timetables.timetable.Timetable> {

    private String id;
    private TimetablesApplication app;
    private TimetableRepository repository;

    public TimetableResource() {
        addToContext(ResourceContextId.LINK_TITLE, "details");
        addToContext(ResourceContextId.LINK_GLYPH, "search");
    }

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (TimetablesApplication) getApplication();
        repository = (TimetableRepository) app.getRepository(io.skysail.server.app.timetables.timetable.Timetable.class);
    }


    @Override
    public SkysailResponse<?> eraseEntity() {
        repository.delete(id);
        return new SkysailResponse<>();
    }

    @Override
    public io.skysail.server.app.timetables.timetable.Timetable getEntity() {
        return (io.skysail.server.app.timetables.timetable.Timetable)app.getRepository().findOne(id);
    }

	@Override
    public List<Link> getLinks() {
        return super.getLinks(PutTimetableResourceGen.class,PostCourseResource.class,CoursesResourceGen.class, PostTimetableToNewCourseRelationResource.class, TimetablesCoursesResource.class);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(TimetablesResource.class);
    }


}