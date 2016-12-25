package io.skysail.server.app.notes;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.skysail.domain.Identifiable;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.notes.Note.BackupStatus;
import io.skysail.server.app.notes.log.EventLog;
import io.skysail.server.restlet.resources.PostEntityServerResource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UpdateRequestResource extends PostEntityServerResource<Event> {

    private NotesApplication app;
    
    private ObjectMapper mapper = new ObjectMapper();
    
    public UpdateRequestResource() {
        addToContext(ResourceContextId.LINK_TITLE, "update from AWS");
    }

    @Override
    protected void doInit() {
        app = (NotesApplication) getApplication();
    }

    @Override
    public Event createEntityTemplate() {
        return new Event();
    }

    @Override
    public void addEntity(Event entity) {
        List<Event> events = app.getEventRepo().findAll();
        
        boolean success = true;
        
        for (Event e : events) {
            if ("CREATED".equals(e.getType())) {
                try {
                    Note note = mapper.readValue(e.getEntity(), Note.class);
                    note.setId(null);
                    app.getRepo().save(note, getApplicationModel());
                } catch (IOException e1) {
                    success = false;
                    log.error(e1.getMessage(),e1);
                    EventLog eventlog = new EventLog();
                    eventlog.setTstamp(new Date().getTime());
                    eventlog.setType(EventLog.TYPE.ERROR);
                    eventlog.setMessage(e1.getMessage());
                    app.getEventLogRepo().save(eventlog, getApplicationModel());
                }
            }
        }
        
        if (success) {
            EventLog eventlog = new EventLog();
            eventlog.setTstamp(new Date().getTime());
            eventlog.setType(EventLog.TYPE.SYNCED);
            eventlog.setMessage("synced");
            app.getEventLogRepo().save(eventlog, getApplicationModel());            
        }

        
    }


}
