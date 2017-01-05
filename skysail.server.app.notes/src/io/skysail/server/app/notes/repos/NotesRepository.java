package io.skysail.server.app.notes.repos;

import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.app.notes.domain.Note;
import io.skysail.server.db.DbClassName;
import io.skysail.server.db.DbService;
import io.skysail.server.db.GraphDbRepository;

public class NotesRepository extends GraphDbRepository<Note> implements DbRepository {

    public NotesRepository(DbService dbService) {
        this.dbService = dbService;
        activate();
    }

    public void activate() {
        dbService.createWithSuperClass("V", DbClassName.of(Note.class));
        dbService.register(Note.class);
    }

}