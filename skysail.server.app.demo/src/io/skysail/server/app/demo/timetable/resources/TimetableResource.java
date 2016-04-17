package io.skysail.server.app.demo.timetable.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.demo.DemoApplication;
import io.skysail.server.app.demo.timetable.PostTimetableToNewCourseRelationResource;
import io.skysail.server.app.demo.timetable.Timetable;
import io.skysail.server.app.demo.timetable.TimetableRepository;
import io.skysail.server.app.demo.timetable.TimetablesCoursesResource;
import io.skysail.server.app.demo.timetable.course.resources.CoursesResourceGen;
import io.skysail.server.app.demo.timetable.course.resources.PostCourseResource;
import io.skysail.server.restlet.resources.EntityServerResource;


public class TimetableResource extends EntityServerResource<Timetable> {

    private String id;
    private DemoApplication app;
    private TimetableRepository repository;

    public TimetableResource() {
        addToContext(ResourceContextId.LINK_TITLE, "details");
        addToContext(ResourceContextId.LINK_GLYPH, "search");
        setDescription("a timetable resource");
    }

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (DemoApplication) getApplication();
        repository = (TimetableRepository) app.getRepository(Timetable.class);
    }


    @Override
    public SkysailResponse<?> eraseEntity() {
        repository.delete(id);
        return new SkysailResponse<>();
    }

    @Override
    public Timetable getEntity() {
        return (Timetable)app.getRepository().findOne(id);
    }

	@Override
    public List<Link> getLinks() {
        return super.getLinks(PutTimetableResource.class,PostCourseResource.class,CoursesResourceGen.class, PostTimetableToNewCourseRelationResource.class, TimetablesCoursesResource.class);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(TimetablesResource.class);
    }


}