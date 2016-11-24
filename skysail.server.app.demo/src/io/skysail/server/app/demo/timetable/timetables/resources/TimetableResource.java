package io.skysail.server.app.demo.timetable.timetables.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.demo.DemoApplication;
import io.skysail.server.app.demo.timetable.course.resources.PostTimetableToNewCourseRelationResource;
import io.skysail.server.app.demo.timetable.course.resources.TimetablesCoursesResource;
import io.skysail.server.app.demo.timetable.repo.TimetableRepository;
import io.skysail.server.app.demo.timetable.timetables.Timetable;
import io.skysail.server.restlet.resources.EntityServerResource;


public class TimetableResource extends EntityServerResource<Timetable> {

    private String id;
    private DemoApplication app;

    public TimetableResource() {
        addToContext(ResourceContextId.LINK_TITLE, "details");
        addToContext(ResourceContextId.LINK_GLYPH, "search");
        setDescription("a timetable resource");
    }

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (DemoApplication) getApplication();
    }


    @Override
    public SkysailResponse<?> eraseEntity() {
    	app.getTtRepo().delete(id);
        return new SkysailResponse<>();
    }

    @Override
    public Timetable getEntity() {
        return app.getTtRepo().findOne(id);
    }

	@Override
    public List<Link> getLinks() {
        return super.getLinks(PutTimetableResource.class,PostTimetableToNewCourseRelationResource.class, TimetablesCoursesResource.class);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(TimetablesResource.class);
    }


}