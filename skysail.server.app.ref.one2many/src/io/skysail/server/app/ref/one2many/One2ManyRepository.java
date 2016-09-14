package io.skysail.server.app.ref.one2many;


import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.db.DbClassName;
import io.skysail.server.db.DbService;
import io.skysail.server.db.GraphDbRepository;

@Component(immediate = true, property = "name=One2ManyRepository")
public class One2ManyRepository extends GraphDbRepository<TodoList> implements DbRepository {

    @Reference
    public void setDbService(DbService dbService) {
        this.dbService = dbService;
    }

    public void unsetDbService(DbService dbService) {
        this.dbService = null;
    }

    @Activate
    public void activate() {
        dbService.createWithSuperClass("V", DbClassName.of(TodoList.class), DbClassName.of(Todo.class));
        dbService.register(TodoList.class, Todo.class);
    }

}