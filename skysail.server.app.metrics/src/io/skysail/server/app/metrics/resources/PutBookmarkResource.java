//package io.skysail.server.app.metrics.resources;
//
//import org.restlet.resource.ResourceException;
//
//import io.skysail.server.app.metrics.Bookmark;
//import io.skysail.server.app.metrics.MetricsApplication;
//import io.skysail.server.restlet.resources.PutEntityServerResource;
//
//public class PutBookmarkResource extends PutEntityServerResource<Bookmark> {
//
//    protected String id;
//    protected MetricsApplication app;
//
//    @Override
//    protected void doInit() throws ResourceException {
//        id = getAttribute("id");
//        app = (MetricsApplication)getApplication();
//    }
//
//    @Override
//    public void updateEntity(Bookmark  entity) {
//        Bookmark original = getEntity();
//        copyProperties(original,entity);
//
//        app.get.update(original,app.getApplicationModel());
//    }
//
//    @Override
//    public Bookmark getEntity() {
//        return (Bookmark)app.getRepository(Bookmark.class).findOne(id);
//    }
//
//    @Override
//    public String redirectTo() {
//        return super.redirectTo(TimersResource.class);
//    }
//}