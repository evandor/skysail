package io.skysail.server.app.timetables.timetable.resources;

import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.api.links.Link;

import java.util.*;

import io.skysail.server.ResourceContextId;
import io.skysail.server.app.timetables.*;

import io.skysail.server.app.timetables.timetable.*;
import io.skysail.server.app.timetables.timetable.resources.*;
import io.skysail.server.app.timetables.course.*;
import io.skysail.server.app.timetables.course.resources.*;


public class TimetablesResource extends ListServerResource<io.skysail.server.app.timetables.timetable.Timetable> {

    private TimetablesApplication app;
    private TimetableRepository repository;

    public TimetablesResource() {
        super(TimetableResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "list Timetables");
    }

    public TimetablesResource(Class<? extends TimetableResource> cls) {
        super(cls);
    }

    @Override
    protected void doInit() {
        app = (TimetablesApplication) getApplication();
        repository = (TimetableRepository) app.getRepository(io.skysail.server.app.timetables.timetable.Timetable.class);
    }

    @Override
    public List<io.skysail.server.app.timetables.timetable.Timetable> getEntity() {
        Filter filter = new Filter(getRequest());
        Pagination pagination = new Pagination(getRequest(), getResponse(), repository.count(filter));
        return repository.find(filter, pagination);
    }

    @Override
    public List<Link> getLinks() {
              return super.getLinks(PostTimetableResourceGen.class,TimetablesResource.class);
    }
}