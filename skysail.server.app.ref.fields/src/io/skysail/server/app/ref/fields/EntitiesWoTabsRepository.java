//package io.skysail.server.app.ref.fields;
//
//import io.skysail.domain.core.repos.DbRepository;
//import io.skysail.server.db.DbClassName;
//import io.skysail.server.db.DbService;
//import io.skysail.server.db.GraphDbRepository;
//
//public class EntitiesWoTabsRepository extends GraphDbRepository<EntityWithoutTabs> implements DbRepository {
//
//    public EntitiesWoTabsRepository (DbService dbService) {
//        this.dbService = dbService;
//        activate();
//    }
//
//    public void activate() {
//        dbService.createWithSuperClass("V", DbClassName.of(EntityWithoutTabs.class));
//        dbService.register(EntityWithoutTabs.class);
//    }
//
//}