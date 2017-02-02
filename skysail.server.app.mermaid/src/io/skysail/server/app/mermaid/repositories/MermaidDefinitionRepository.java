package io.skysail.server.app.mermaid.repositories;

import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.app.mermaid.MermaidDefinition;
import io.skysail.server.db.DbClassName;
import io.skysail.server.db.DbService;
import io.skysail.server.db.GraphDbRepository;

public class MermaidDefinitionRepository extends GraphDbRepository<MermaidDefinition> implements DbRepository {

    public MermaidDefinitionRepository (DbService dbService) {
        this.dbService = dbService;
        activate();
    }

    public void activate() {
        dbService.createWithSuperClass("V", DbClassName.of(MermaidDefinition.class));
        dbService.register(MermaidDefinition.class);
    }

}
