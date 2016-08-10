package io.skysail.server.app.ref.one2many.noagg.resources;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.ref.one2many.noagg.Contact;
import io.skysail.server.restlet.resources.EntityServerResource;

public class CompanysContactResource extends EntityServerResource<Contact> {

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