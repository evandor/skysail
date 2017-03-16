package io.skysail.server.app.ref.one2one.resources;
import io.skysail.server.app.ref.one2one.Detail;
import io.skysail.server.restlet.resources.EntityServerResource;

public class MastersDetailResource extends EntityServerResource<Detail> {

    @Override
    public Detail getEntity() {
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