package io.skysail.server.app.demo.timetable.timetables.resources;

import org.restlet.resource.ResourceException;

import io.skysail.server.ResourceContextId;
import io.skysail.server.app.demo.DemoApplication;
import io.skysail.server.app.demo.timetable.timetables.Timetable;
import io.skysail.server.restlet.resources.PutEntityServerResource;

public class PutTimetableResource extends PutEntityServerResource<Timetable> {

    protected String id;
    protected DemoApplication app;

    public PutTimetableResource() {
        setDescription("lets you update a timetable entity");
        addToContext(ResourceContextId.LINK_TITLE, "update Timetable");
    }

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (DemoApplication) getApplication();
    }

    @Override
    public void updateEntity(Timetable entity) {
        Timetable original = getEntity();
        copyProperties(original, entity);

        app.getTtRepo().update(original, app.getApplicationModel());
    }

    @Override
    public Timetable getEntity() {
        return app.getTtRepo().findOne(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(TimetablesResource.class);
    }
}