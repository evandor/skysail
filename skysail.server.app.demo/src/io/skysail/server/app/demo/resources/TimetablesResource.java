//package io.skysail.server.app.demo.resources;
//
//import java.util.List;
//
//import io.skysail.api.links.Link;
//import io.skysail.server.ResourceContextId;
//import io.skysail.server.app.demo.DemoApplication;
//import io.skysail.server.app.demo.DemoRepository;
//import io.skysail.server.app.demo.RamlClientResource;
//import io.skysail.server.app.demo.Timetable;
//import io.skysail.server.app.demo.UnprotectedTimesResource;
//import io.skysail.server.queryfilter.Filter;
//import io.skysail.server.queryfilter.pagination.Pagination;
//import io.skysail.server.restlet.resources.ListServerResource;
//
//public class TimetablesResource extends ListServerResource<Timetable> {
//
//    private DemoApplication app;
//    private DemoRepository repository;
//
//    public TimetablesResource() {
//        super(TimetableResource.class);
//        addToContext(ResourceContextId.LINK_TITLE, "list Timetables");
//    }
//
//    public TimetablesResource(Class<? extends TimetableResource> cls) {
//        super(cls);
//    }
//
//    @Override
//    protected void doInit() {
//        app = (DemoApplication) getApplication();
//        repository = (DemoRepository) app.getRepository(Timetable.class);
//    }
//
//    @Override
//    @SuppressWarnings("unchecked")
//    public List<Timetable> getEntity() {
//        Filter filter = new Filter(getRequest());
//        Pagination pagination = new Pagination(getRequest(), getResponse(), repository.count(filter));
//        return repository.find(filter, pagination);
//    }
//
//    @Override
//    public List<Link> getLinks() {
//              return super.getLinks(PostTimetableResource.class,TimetablesResource.class, RamlClientResource.class);
//    }
//}