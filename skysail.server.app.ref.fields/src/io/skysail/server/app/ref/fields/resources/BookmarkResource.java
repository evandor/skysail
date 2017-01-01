//package io.skysail.server.app.ref.fields.resources;
//
//import java.util.List;
//
//import io.skysail.api.links.Link;
//import io.skysail.api.responses.SkysailResponse;
//import io.skysail.server.ResourceContextId;
//import io.skysail.server.app.ref.fields.EntityWithTabs;
//import io.skysail.server.app.ref.fields.FieldsDemoApplication;
//import io.skysail.server.app.ref.fields.FieldsDemoRepository;
//import io.skysail.server.restlet.resources.EntityServerResource;
//
//public class BookmarkResource extends EntityServerResource<EntityWithTabs> {
//
//    private String id;
//    private FieldsDemoApplication app;
//
//    public BookmarkResource() {
//        addToContext(ResourceContextId.LINK_TITLE, "details");
//        addToContext(ResourceContextId.LINK_GLYPH, "search");
//    }
//
//    @Override
//    protected void doInit() {
//        id = getAttribute("id");
//        app = (FieldsDemoApplication) getApplication();
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
//    public EntityWithTabs getEntity() {
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