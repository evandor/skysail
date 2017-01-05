package io.skysail.server.app.notes.service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.skysail.server.app.notes.NotesApplication;
import io.skysail.server.app.notes.domain.AwsNote;
import io.skysail.server.app.notes.domain.Note;
import io.skysail.server.app.notes.domain.Note.BackupStatus;
import io.skysail.server.queryfilter.filtering.Filter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AwsSyncService {

    private ObjectMapper mapper = new ObjectMapper();
    private NotesApplication app;

    public AwsSyncService(NotesApplication notesApplication) {
        this.app = notesApplication;

        // schedule update from AWS...
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.schedule(new Runnable() {
            @Override
            public void run() {
                updateFromAws();
            }
        }, 15, TimeUnit.MINUTES);

        // ... and run now.
        //updateFromAws();
    }

    public void updateFromAws() {
        List<AwsNote> events = app.getEventRepo().findAll();

        boolean success = true;

        for (AwsNote e : events) {
            try {
                Note note = mapper.readValue(e.getEntity(), Note.class);
                note.setId(null);
                note.setBackupStatus(BackupStatus.SYNCED);

                List<Note> localNotes = app.getRepo().find(new Filter("(uuid="+note.getUuid()+")"));
                if (localNotes.size() == 0) {
                    app.getRepo().save(note, app.getApplicationModel());
                } else if (localNotes.size()==1 && localNotes.get(0).getModified() != null) {
                    if (localNotes.get(0).getModified().before(note.getModified())) {
                        app.getRepo().update(note, app.getApplicationModel());
                    }
                }
            } catch (IOException e1) {
                success = false;
                log.error(e1.getMessage(), e1);
//                EventLog eventlog = new EventLog();
//                eventlog.setTstamp(new Date().getTime());
//                eventlog.setType(EventLog.TYPE.ERROR);
//                eventlog.setMessage(e1.getMessage());
//                app.getEventLogRepo().save(eventlog, getApplicationModel());
            }
        }

        if (success) {
//            EventLog eventlog = new EventLog();
//            eventlog.setTstamp(new Date().getTime());
//            eventlog.setType(EventLog.TYPE.SYNCED);
//            eventlog.setMessage("synced");
//            app.getEventLogRepo().save(eventlog, getApplicationModel());
        }

    }

}
