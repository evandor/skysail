//package io.skysail.server.app.ref.fields.resources;
//
//import java.util.List;
//
//import io.skysail.api.links.Link;
//import io.skysail.server.ResourceContextId;
//import io.skysail.server.app.ref.fields.EntityWithTabs;
//import io.skysail.server.app.ref.fields.FieldsDemoApplication;
//import io.skysail.server.queryfilter.filtering.Filter;
//import io.skysail.server.queryfilter.pagination.Pagination;
//import io.skysail.server.restlet.resources.ListServerResource;
//
//public class BookmarksResource extends ListServerResource<EntityWithTabs> {
//
//    private FieldsDemoApplication app;
//
//    public BookmarksResource() {
//        super(BookmarkResource.class);
//        addToContext(ResourceContextId.LINK_TITLE, "list Bookmarks");
//    }
//
//    public BookmarksResource(Class<? extends BookmarkResource> cls) {
//        super(cls);
//    }
//
//    @Override
//    protected void doInit() {
//        app = (FieldsDemoApplication) getApplication();
//    }
//
//    @Override
//    public List<EntityWithTabs> getEntity() {
//        Filter filter = new Filter(getRequest());
//        Pagination pagination = new Pagination(getRequest(), getResponse());
//        return app.getRepo().find(filter, pagination);
//    }
//
//    @Override
//    public List<Link> getLinks() {
//        return super.getLinks(PostBookmarkResource.class, BookmarksResource.class, EntitiesWithoutTabsResource.class);
//    }
//}