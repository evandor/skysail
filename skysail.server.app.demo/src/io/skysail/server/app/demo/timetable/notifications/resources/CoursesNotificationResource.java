package io.skysail.server.app.demo.timetable.notifications.resources;
import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.app.demo.PutTimetablesCourseResource;
import io.skysail.server.app.demo.timetable.notifications.Notification;
import io.skysail.server.restlet.resources.EntityServerResource;

public class CoursesNotificationResource extends EntityServerResource<Notification> {

    @Override
    public Notification getEntity() {
        return null;
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(PutTimetablesCourseResource.class,CoursesNotificationsResource.class);
    }

}