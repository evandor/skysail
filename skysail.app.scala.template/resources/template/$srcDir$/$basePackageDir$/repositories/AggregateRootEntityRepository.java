package $basePackageName$.repositories;

import io.skysail.domain.core.repos.DbRepository;
import  $basePackageName$.AggregateRootEntity;
import io.skysail.server.db.DbClassName;
import io.skysail.server.db.DbService;
import io.skysail.server.db.GraphDbRepository;

public class AggregateRootEntityRepository extends GraphDbRepository<AggregateRootEntity> implements DbRepository {

    public AggregateRootEntityRepository (DbService dbService) {
        this.dbService = dbService;
        activate();
    }

    public void activate() {
        dbService.createWithSuperClass("V", DbClassName.of(AggregateRootEntity.class));
        dbService.register(AggregateRootEntity.class);
    }

}
