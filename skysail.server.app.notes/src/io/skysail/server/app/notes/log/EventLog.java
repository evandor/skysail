package io.skysail.server.app.notes.log;

import io.skysail.domain.Entity;
import lombok.Data;

@Data
public class EventLog implements Entity {

    public enum TYPE {
        ERROR,
        CREATED, SYNCED
    }

    private String id;
    
    private long tstamp;
    
    private TYPE type;
    
    private String message;
}
