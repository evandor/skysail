package io.skysail.server.app.notes.log;

import io.skysail.domain.Identifiable;
import lombok.Data;

@Data
public class EventLog implements Identifiable {

    public enum TYPE {
        ERROR,
        CREATED, SYNCED
    }

    private String id;
    
    private long tstamp;
    
    private TYPE type;
    
    private String message;
}
