package io.skysail.server.app.demo.timetable.resources;

import org.restlet.resource.ResourceException;

import io.skysail.server.ResourceContextId;
import io.skysail.server.app.demo.DemoApplication;
import io.skysail.server.app.demo.timetable.Timetable;
import io.skysail.server.restlet.resources.PostEntityServerResource;

public class PostTimetableResource extends PostEntityServerResource<Timetable> {

	protected DemoApplication app;

    public PostTimetableResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new ");
        setDescription("Let's you add a new timetable");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (DemoApplication) getApplication();
    }

    @Override
    public Timetable createEntityTemplate() {
        return new Timetable();
    }

    @Override
    public void addEntity(Timetable entity) {
        //Subject subject = SecurityUtils.getSubject();
        String id = app.getRepository(Timetable.class).save(entity, app.getApplicationModel()).toString();
        entity.setId(id);

    }

    @Override
    public String redirectTo() {
        return super.redirectTo(TimetablesResource.class);
    }
}