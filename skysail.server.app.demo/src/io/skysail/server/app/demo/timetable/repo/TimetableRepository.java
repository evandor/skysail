package io.skysail.server.app.demo.timetable.repo;

import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.app.demo.timetable.course.Course;
import io.skysail.server.app.demo.timetable.notifications.Notification;
import io.skysail.server.app.demo.timetable.timetables.Timetable;
import io.skysail.server.db.DbClassName;
import io.skysail.server.db.DbService;
import io.skysail.server.db.GraphDbRepository;

public class TimetableRepository extends GraphDbRepository<Timetable> implements DbRepository {

	public TimetableRepository(DbService dbService) {
		this.dbService = dbService;
		activate();
	}

	public void activate() {
		dbService.createWithSuperClass("V", DbClassName.of(Timetable.class), DbClassName.of(Course.class),
				DbClassName.of(Notification.class));
		dbService.register(Timetable.class, Course.class, Notification.class);
	}

}