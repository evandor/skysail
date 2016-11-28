package io.skysail.server.app.demo.timetable.notifications.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.demo.DemoApplication;
import io.skysail.server.app.demo.timetable.course.Course;
import io.skysail.server.app.demo.timetable.course.resources.TimetablesCoursesResource;
import io.skysail.server.app.demo.timetable.notifications.Notification;
import io.skysail.server.app.demo.timetable.repo.TimetableRepository;
import io.skysail.server.app.demo.timetable.timetables.Timetable;
import io.skysail.server.app.demo.timetable.timetables.resources.TimetablesResource;
import io.skysail.server.restlet.resources.PostRelationResource2;


public class PostCourseToNewNotificationRelationResource extends PostRelationResource2<Notification> {

    private DemoApplication app;
    private String timetableId;

    public PostCourseToNewNotificationRelationResource() {
        addToContext(ResourceContextId.LINK_TITLE, "create new notification for this course");
    }

    @Override
    protected void doInit() {
        app = (DemoApplication) getApplication();
        timetableId = getAttribute("id");
    }

    @Override
    public Notification createEntityTemplate() {
        return new Notification();
    }

    @Override
    public void addEntity(Notification notification) {
        Timetable timetable = app.getTtRepo().findOne(timetableId);
        Course course = timetable.getCourse(getAttribute("courseId"));
        course.addNotification(notification);
        app.getTtRepo().save(timetable, getApplication().getApplicationModel());
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(TimetablesCoursesResource.class, PostCourseToNewNotificationRelationResource.class);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(TimetablesResource.class);
    }
}