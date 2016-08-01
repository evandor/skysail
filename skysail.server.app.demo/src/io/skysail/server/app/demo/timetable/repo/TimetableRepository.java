package io.skysail.server.app.demo.timetable.repo;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.app.demo.timetable.course.Course;
import io.skysail.server.app.demo.timetable.notifications.Notification;
import io.skysail.server.app.demo.timetable.timetables.Timetable;
import io.skysail.server.db.DbClassName;
import io.skysail.server.db.DbService;
import io.skysail.server.db.GraphDbRepository;

@Component(immediate = true, property = "name=TimetablesRepository")
public class TimetableRepository extends GraphDbRepository<Timetable> implements DbRepository {

    @Reference
    public void setDbService(DbService dbService) {
        this.dbService = dbService;
    }

    public void unsetDbService(DbService dbService) {
        this.dbService = null;
    }

    @Activate
    public void activate() {
        dbService.createWithSuperClass("V",
                DbClassName.of(Timetable.class),
                DbClassName.of(Course.class),
                DbClassName.of(Notification.class));
        dbService.register(
                Timetable.class,
                Course.class,
                Notification.class);
    }

}