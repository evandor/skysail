//package io.skysail.server.app.notes.repos;
//
//import org.osgi.service.event.EventAdmin;
//
//import io.skysail.domain.core.repos.DbRepository;
//import io.skysail.server.app.notes.log.EventLog;
//import io.skysail.server.db.DbClassName;
//import io.skysail.server.db.DbService;
//import io.skysail.server.db.GraphDbRepository;
//
//public class EventLogRepository extends GraphDbRepository<EventLog> implements DbRepository {
//
//    public EventLogRepository(DbService dbService, EventAdmin eventAdmin) {
//        this.dbService = dbService;
//        activate();
//    }
//
//    public void activate() {
//        dbService.createWithSuperClass("V", DbClassName.of(EventLog.class));
//        dbService.register(EventLog.class);
//    }
//
//}