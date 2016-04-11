//package io.skysail.server.app.demo.resources;
//
//import java.util.List;
//
//import io.skysail.api.links.Link;
//import io.skysail.api.responses.SkysailResponse;
//import io.skysail.server.ResourceContextId;
//import io.skysail.server.app.demo.DemoApplication;
//import io.skysail.server.app.demo.DemoRepository;
//import io.skysail.server.app.demo.Timetable;
//import io.skysail.server.restlet.resources.EntityServerResource;
//
//public class TimetableResource extends EntityServerResource<Timetable> {
//
//    private String id;
//    private DemoApplication app;
//    private DemoRepository repository;
//
//    public TimetableResource() {
//        addToContext(ResourceContextId.LINK_TITLE, "details");
//        addToContext(ResourceContextId.LINK_GLYPH, "search");
//    }
//
//    @Override
//    protected void doInit() {
//        id = getAttribute("id");
//        app = (DemoApplication) getApplication();
//        repository = (DemoRepository) app.getRepository(Timetable.class);
//    }
//
//
//    @Override
//    public SkysailResponse<?> eraseEntity() {
//        repository.delete(id);
//        return new SkysailResponse<>();
//    }
//
//    @Override
//    public Timetable getEntity() {
//        return (Timetable)app.getRepository().findOne(id);
//    }
//
//	@Override
//    public List<Link> getLinks() {
//        return super.getLinks(PutTimetableResource.class);
//    }
//
//    @Override
//    public String redirectTo() {
//        return super.redirectTo(TimetablesResource.class);
//    }
//
//
//}