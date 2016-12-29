//package io.skysail.server.app.routes.resources;
//
//import java.util.List;
//
//import io.skysail.api.links.Link;
//import io.skysail.api.responses.SkysailResponse;
//import io.skysail.server.ResourceContextId;
//import io.skysail.server.app.routes.RouteDescription;
//import io.skysail.server.app.routes.RoutesApplication;
//import io.skysail.server.app.routes.TemplateRepository;
//import io.skysail.server.restlet.resources.EntityServerResource;
//
//public class BookmarkResource extends EntityServerResource<RouteDescription> {
//
//    private String id;
//    private RoutesApplication app;
//
//    public BookmarkResource() {
//        addToContext(ResourceContextId.LINK_TITLE, "details");
//        addToContext(ResourceContextId.LINK_GLYPH, "search");
//    }
//
//    @Override
//    protected void doInit() {
//        id = getAttribute("id");
//        app = (RoutesApplication) getApplication();
//    }
//
//
//    @Override
//    public SkysailResponse<?> eraseEntity() {
//    	app.getRepo().delete(id);
//        return new SkysailResponse<>();
//    }
//
//    @Override
//    public RouteDescription getEntity() {
//        return app.getRepo().findOne(id);
//    }
//
//	@Override
//    public List<Link> getLinks() {
//        return super.getLinks(PutBookmarkResource.class);
//    }
//
//    @Override
//    public String redirectTo() {
//        return super.redirectTo(BookmarksResource.class);
//    }
//
//
//}