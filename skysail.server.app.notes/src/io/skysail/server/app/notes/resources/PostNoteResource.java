package io.skysail.server.app.notes.resources;

import java.util.Date;
import java.util.UUID;

import org.restlet.resource.ResourceException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

import io.skysail.server.ResourceContextId;
import io.skysail.server.app.notes.domain.AwsNote;
import io.skysail.server.app.notes.domain.Note;
import io.skysail.server.app.notes.domain.AwsNote.AwsStatus;
import io.skysail.server.app.notes.domain.Note.BackupStatus;
import io.skysail.server.app.notes.NotesApplication;
import io.skysail.server.restlet.resources.PostEntityServerResource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PostNoteResource extends PostEntityServerResource<Note> {

    protected NotesApplication app;

    private ObjectMapper mapper = new ObjectMapper();

    public PostNoteResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new Note");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (NotesApplication) getApplication();
    }

    @Override
    public Note createEntityTemplate() {
        return new Note();
    }

    /**
     * successfully creating a note results in a locally save note with status "SYNCED", and
     * a remote copy with status "SYNCING".
     *
     * If saving to AWS fails, the local status is still "SYNCING" (and could be retried).
     *
     * If updating the status locally fails, the remote entry exists, but the local one is still
     * in status "SYNCING".
     */
    @Override
    public void addEntity(Note entity) {
        Note entityToPost = setupNoteForPost(entity); // create UUID, set created date and SYNCING status
        saveLocally(entityToPost);
        try {
            saveToAws(entityToPost);
            updateLocalStatus(entityToPost, BackupStatus.SYNCED);
        } catch (Exception e) {
            log.error("Problem with backup: {}", e.getMessage(), e);
        }
    }

    private Note setupNoteForPost(Note entity) {
        entity.setUuid(UUID.randomUUID().toString());
        entity.setCreated(new Date());
        entity.setBackupStatus(BackupStatus.SYNCING);
        return entity;
    }

    private OrientVertex saveLocally(Note note) {
        OrientVertex id = app.getRepo().save(note, app.getApplicationModel());
        note.setId(id.getId().toString().replace("#",""));
        return id;
    }

    private void saveToAws(Note note) throws JsonProcessingException {
        app.getEventRepo().save(createPostEvent(note), getApplicationModel());
    }

    private void updateLocalStatus(Note entity, BackupStatus status) {
        entity.setBackupStatus(status);
        app.getRepo().update(entity, getApplicationModel());
    }

    private AwsNote createPostEvent(Note entity) throws JsonProcessingException {
        AwsNote awsNote = new AwsNote();
        awsNote.setNoteUuid(entity.getUuid());
        awsNote.setEntity(mapper.writeValueAsString(entity));
        awsNote.setTstamp(System.currentTimeMillis());
        awsNote.setType(AwsStatus.CREATED.name());
        return awsNote;
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(NotesResource.class);
    }

}