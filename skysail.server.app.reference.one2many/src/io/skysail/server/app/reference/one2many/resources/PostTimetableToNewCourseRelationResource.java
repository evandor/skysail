package io.skysail.server.app.reference.one2many.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.demo.timetable.timetables.Timetable;
import io.skysail.server.app.reference.one2many.One2ManyApplication;
import io.skysail.server.app.reference.one2many.One2ManyRepository;
import io.skysail.server.app.reference.one2many.Todo;
import io.skysail.server.app.reference.one2many.TodoList;
import io.skysail.server.restlet.resources.PostRelationResource2;


public class PostTimetableToNewCourseRelationResource extends PostRelationResource2<Todo> {

    private One2ManyApplication app;
    private One2ManyRepository repo;
    private String parentId;

    public PostTimetableToNewCourseRelationResource() {
        addToContext(ResourceContextId.LINK_TITLE, "create new course for this timetable");
    }

    @Override
    protected void doInit() {
        app = (One2ManyApplication) getApplication();
        repo = (One2ManyRepository) app.getRepository(TodoList.class);
        parentId = getAttribute("id");
    }

    @Override
    public Todo createEntityTemplate() {
        return new Todo();
    }

    @Override
    public void addEntity(Todo entity) {
        Timetable parent = repo.findOne(parentId);
        parent.getCourses().add(entity);
        repo.save(parent, getApplication().getApplicationModel());
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(TimetablesCoursesResource.class, PostTimetableToNewCourseRelationResource.class);
    }
}