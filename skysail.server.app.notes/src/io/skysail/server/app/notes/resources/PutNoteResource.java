package io.skysail.server.app.notes.resources;

import java.util.Date;

import org.restlet.resource.ResourceException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.skysail.server.app.notes.domain.AwsNote;
import io.skysail.server.app.notes.domain.Note;
import io.skysail.server.app.notes.domain.AwsNote.AwsStatus;
import io.skysail.server.app.notes.domain.Note.BackupStatus;
import io.skysail.server.app.notes.NotesApplication;
import io.skysail.server.restlet.resources.PutEntityServerResource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PutNoteResource extends PutEntityServerResource<Note> {

    protected String id;
    protected NotesApplication app;

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (NotesApplication)getApplication();
    }

    @Override
    public void updateEntity(Note  entity) {
        Note original = getEntity();
        String uuid = original.getUuid();
        copyProperties(original,entity);

        entity.setUuid(uuid);
        entity.setCreated(original.getCreated());

        Note entityToPut = setupNoteForPut(entity); // set modified and UPDATING status
        updateLocally(entityToPut);
        try {
            saveToAws(entityToPut);
            updateLocalStatus(entityToPut, BackupStatus.SYNCED);
        } catch (Exception e) {
            log.error("Problem with backup: {}", e.getMessage(), e);
        }
    }

    private Note setupNoteForPut(Note entity) {
        entity.setModified(new Date());
        entity.setBackupStatus(BackupStatus.SYNCING);
        return entity;
    }

    private void updateLocally(Note entityToPut) {
        app.getRepo().update(entityToPut,app.getApplicationModel());
    }

    private void saveToAws(Note entityToPut) throws JsonProcessingException {
        app.getEventRepo().update(createPutEvent(entityToPut), getApplicationModel());
    }

    private AwsNote createPutEvent(Note entity)  throws JsonProcessingException {
        AwsNote awsNote = new AwsNote();
        awsNote.setNoteUuid(entity.getUuid());
        awsNote.setEntity(mapper.writeValueAsString(entity));
        awsNote.setTstamp(System.currentTimeMillis());
        awsNote.setType(AwsStatus.UPDATED.name());
        return awsNote;
    }

    private void updateLocalStatus(Note entity, BackupStatus status) {
        entity.setBackupStatus(status);
        app.getRepo().update(entity, getApplicationModel());
    }

    @Override
    public Note getEntity() {
        return app.getRepo().findOne(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(NotesResource.class);
    }
}