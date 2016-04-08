package io.skysail.server.app.demo.resources;

import org.restlet.resource.ResourceException;

import io.skysail.server.ResourceContextId;
import io.skysail.server.app.demo.DemoApplication;
import io.skysail.server.app.demo.Timetable;
import io.skysail.server.restlet.resources.PostEntityServerResource;

public class PostTimetableResourceGen extends PostEntityServerResource<Timetable> {

	protected DemoApplication app;

    public PostTimetableResourceGen() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new ");
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
        String id = app.getRepository(Timetable.class).save(entity, app.getApplicationModel()).toString();
        entity.setId(id);

    }

    @Override
    public String redirectTo() {
        return super.redirectTo(TimetablesResourceGen.class);
    }
}