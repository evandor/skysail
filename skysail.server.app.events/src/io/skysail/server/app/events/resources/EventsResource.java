package io.skysail.server.app.events.resources;

import java.util.List;

import org.restlet.resource.ResourceException;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.events.EventsApplication;
import io.skysail.server.app.events.domain.EventDesc;
import io.skysail.server.restlet.resources.ListServerResource;

public class EventsResource extends ListServerResource<EventDesc> {

    private EventsApplication app;

    @Override
    protected void doInit() throws ResourceException {
        app = (EventsApplication) getApplication();
    }
    
    @Override
    public List<?> getEntity() {
        return app.getEvents();
    }
    
    public SkysailResponse eraseEntity() {
        app.clearEvents();
        return new SkysailResponse<>();
    }

}
