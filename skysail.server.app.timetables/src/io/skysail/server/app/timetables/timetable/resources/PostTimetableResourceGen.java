package io.skysail.server.app.timetables.timetable.resources;

import org.restlet.resource.ResourceException;

import io.skysail.server.ResourceContextId;
import io.skysail.server.app.timetables.TimetablesApplication;
import io.skysail.server.app.timetables.timetable.Timetable;
import io.skysail.server.restlet.resources.PostEntityServerResource;

/**
 * generated from postResource.stg
 */
public class PostTimetableResourceGen extends PostEntityServerResource<io.skysail.server.app.timetables.timetable.Timetable> {

	protected TimetablesApplication app;

    public PostTimetableResourceGen() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new ");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (TimetablesApplication) getApplication();
    }

    @Override
    public io.skysail.server.app.timetables.timetable.Timetable createEntityTemplate() {
        return new Timetable();
    }

    @Override
    public void addEntity(io.skysail.server.app.timetables.timetable.Timetable entity) {
        //Subject subject = SecurityUtils.getSubject();
        String id = app.getRepository(io.skysail.server.app.timetables.timetable.Timetable.class).save(entity, app.getApplicationModel()).toString();
        entity.setId(id);

    }

    @Override
    public String redirectTo() {
        return super.redirectTo(TimetablesResource.class);
    }
}