package io.skysail.server.app.demo.timetable;

import java.util.List;

import io.skysail.server.app.demo.DemoApplication;
import io.skysail.server.app.demo.timetable.course.Course;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.restlet.resources.PostRelationResource;


public class PostTimetablesCourseRelationResource extends PostRelationResource<Timetable, Course> {

    private DemoApplication app;
    private TimetableRepository TimetableRepo;

    public PostTimetablesCourseRelationResource() {
    }

    @Override
    protected void doInit() {
        app = (DemoApplication) getApplication();
     //   CourseRepo = (CourseRepository) app.getRepository(io.skysail.server.app.timetables.course.Course.class);
        //userRepo = (UserRepository) app.getRepository(io.skysail.server.app.oEService.User.class);
    }

    @Override
    public List<Course> getEntity() {
        Filter filter = new Filter(getRequest());
       // Pagination pagination = new Pagination(getRequest(), getResponse(), CourseRepo.count(filter));
        return null;//CourseRepo.find(filter, pagination);
    }

    @Override
    protected List<Course> getRelationTargets(String selectedValues) {
        Filter filter = new Filter(getRequest());
        //Pagination pagination = new Pagination(getRequest(), getResponse(), CourseRepo.count(filter));
        return null;//CourseRepo.find(filter, pagination);//.stream().filter(predicate);
    }

    @Override
    public void addRelations(List<Course> entities) {
        String id = getAttribute("id");
        Timetable theUser = TimetableRepo.findOne(id);
        entities.stream().forEach(e -> addIfNotPresentYet(theUser, e));
        //CourseRepo.save(theUser, getApplication().getApplicationModel());
    }

    private void addIfNotPresentYet(Timetable theUser, Course e) {
        if (!theUser.getCourses().stream().filter(oe -> oe.getId().equals(oe.getId())).findFirst().isPresent()) {
            theUser.getCourses().add(e);
        }
    }



//    @Override
//    public String redirectTo() {
//        return super.redirectTo(UsersResource.class);
//    }


}