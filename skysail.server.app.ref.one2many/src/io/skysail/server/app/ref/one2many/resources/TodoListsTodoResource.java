package io.skysail.server.app.ref.one2many.resources;
import io.skysail.server.app.ref.one2many.Todo;
import io.skysail.server.restlet.resources.EntityServerResource;

public class TodoListsTodoResource extends EntityServerResource<Todo> {

    @Override
    public Todo getEntity() {
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