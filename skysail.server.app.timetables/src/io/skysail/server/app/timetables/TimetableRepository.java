package io.skysail.server.app.timetables;


import org.osgi.service.component.annotations.*;

import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.app.timetables.course.Course;
import io.skysail.server.app.timetables.timetable.Timetable;
import io.skysail.server.db.*;

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
        		DbClassName.of(Course.class)
        	);
        dbService.register(
        		Timetable.class,
        		Course.class
        	);
    }

}