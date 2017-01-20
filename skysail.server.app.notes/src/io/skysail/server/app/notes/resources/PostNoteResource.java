package io.skysail.server.app.notes.resources;

import java.util.Date;
import java.util.UUID;

import org.restlet.resource.ResourceException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

import io.skysail.server.ResourceContextId;
import io.skysail.server.app.notes.Note;
import io.skysail.server.app.notes.Note.BackupStatus;
import io.skysail.server.app.notes.NotesApplication;
import io.skysail.server.restlet.resources.PostEntityServerResource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PostNoteResource extends PostEntityServerResource<Note> {

    protected NotesApplication app;
    
    private ObjectMapper mapper = new ObjectMapper();

    public PostNoteResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new");
        setDescription(
                "adds a Note to the local repository as well as to AWS dynamicDB. A UUID is assigned to "
              + "the provided note entity and the creation date is set; the BackupStatus defaults to NONE. "
              + "After the note was persisted in skysail, the BackupStatus is set to CREATING and an "
              + "attemp is made to persist the entity in AWS. If this is successfull, the note is updated "
              + "locally to a BackupStatus of CREATED."
                        );
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (NotesApplication) getApplication();
    }

    @Override
    public Note createEntityTemplate() {
        return new Note();
    }

    @Override
    public void addEntity(Note entity) {
    	
    	//Subject subject = SecurityUtils.getSubject();
    	
        entity.setUuid(UUID.randomUUID().toString());
        entity.setCreated(new Date());
        OrientVertex id = app.getRepo().save(entity, app.getApplicationModel());
        entity.setId(id.getId().toString());
        entity.setBackupStatus(BackupStatus.CREATING);
        try {
            PostEvent postEvent = new PostEvent(entity);
            postEvent.setId(id.getId().toString());
            postEvent.setUserUuid("11d2741c-baeb-45bf-8bdd-8a58e983a777");
            postEvent.setEntity(mapper.writeValueAsString(entity));
            postEvent.setTstamp(System.currentTimeMillis());
			app.getEventRepo().save(postEvent, getApplicationModel());
            entity.setBackupStatus(BackupStatus.CREATED);
            app.getRepo().update(entity, getApplicationModel());
        } catch (Exception e) {
        	log.error("Problem with backup: {}", e.getMessage(), e);
        }
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(NotesResource.class);
    }

}