package io.skysail.server.app.reference.one.resources;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.reference.one.Contact;
import io.skysail.server.restlet.resources.EntityServerResource;

public class TodoListsTodoResource extends EntityServerResource<Contact> {

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

    @Override
    public Contact getEntity() {
        return null;
    }

//    @Override
//    public List<Link> getLinks() {
//        return super.getLinks(
//                PutTimetablesCourseResource.class,
//                PostCourseToNewNotificationRelationResource.class,
//                CoursesNotificationsResource.class);
//    }

}