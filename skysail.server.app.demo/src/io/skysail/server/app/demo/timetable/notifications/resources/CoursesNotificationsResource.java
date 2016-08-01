package io.skysail.server.app.demo.timetable.notifications.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.demo.DemoApplication;
import io.skysail.server.app.demo.timetable.notifications.Notification;
import io.skysail.server.app.demo.timetable.repo.TimetableRepository;
import io.skysail.server.app.demo.timetable.timetables.Timetable;
import io.skysail.server.db.DbClassName;
import io.skysail.server.restlet.resources.ListServerResource;


public class CoursesNotificationsResource extends ListServerResource<Notification> {

    private DemoApplication app;
	private TimetableRepository repo;

    public CoursesNotificationsResource() {
        super(CoursesNotificationResource.class);
        addToContext(ResourceContextId.LINK_GLYPH, "list");
        addToContext(ResourceContextId.LINK_TITLE, "list notifications for this course");
    }

    @Override
    protected void doInit() {
        app = (DemoApplication) getApplication();
        repo = (TimetableRepository) app.getRepository(Timetable.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Notification> getEntity() {
        String sql = "select * from " + DbClassName.of(Notification.class) + " where #"+getAttribute("courseId")+" in IN(notifications)";
        return (List<Notification>) repo.execute(Notification.class, sql);
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(CoursesNotificationsResource.class);//, PostTimetablesCourseRelationResource.class);
    }
}