package io.skysail.server.app.ref.one2many;


import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.db.DbClassName;
import io.skysail.server.db.DbService;
import io.skysail.server.db.GraphDbRepository;

public class One2ManyRepository extends GraphDbRepository<TodoList> implements DbRepository {

    public One2ManyRepository(DbService dbService) {
        this.dbService = dbService;
        activate();
    }

    public void activate() {
        dbService.createWithSuperClass("V", DbClassName.of(TodoList.class), DbClassName.of(Todo.class));
        dbService.register(TodoList.class, Todo.class);
    }

}