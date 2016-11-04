package io.skysail.server.app.demo.timetable.timetables.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.demo.DemoApplication;
import io.skysail.server.app.demo.timetable.repo.TimetableRepository;
import io.skysail.server.app.demo.timetable.timetables.Timetable;
import io.skysail.server.queryfilter.filtering.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;


public class TimetablesResource extends ListServerResource<Timetable> {

    private DemoApplication app;
    private TimetableRepository repository;

    public TimetablesResource() {
        super(TimetableResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "list Timetables");
        setDescription("Provides the list of timetables");
    }

    public TimetablesResource(Class<? extends TimetableResource> cls) {
        super(cls);
    }

    @Override
    protected void doInit() {
        app = (DemoApplication) getApplication();
        repository = (TimetableRepository) app.getRepository(Timetable.class);
    }

    @Override
    public List<Timetable> getEntity() {
        Filter filter = new Filter(getRequest());
        Pagination pagination = new Pagination(getRequest(), getResponse());
        return repository.find(filter, pagination);
    }

    @Override
    public List<Link> getLinks() {
              return super.getLinks(PostTimetableResource.class,TimetablesResource.class);
    }
}