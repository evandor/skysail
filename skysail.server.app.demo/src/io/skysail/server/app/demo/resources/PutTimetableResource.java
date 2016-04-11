//package io.skysail.server.app.demo.resources;
//
//import org.restlet.resource.ResourceException;
//
//import io.skysail.server.app.demo.DemoApplication;
//import io.skysail.server.app.demo.Timetable;
//import io.skysail.server.restlet.resources.PutEntityServerResource;
//
//public class PutTimetableResource extends PutEntityServerResource<Timetable> {
//
//    protected String id;
//    protected DemoApplication app;
//
//    @Override
//    protected void doInit() throws ResourceException {
//        id = getAttribute("id");
//        app = (DemoApplication)getApplication();
//    }
//
//    @Override
//    public void updateEntity(Timetable  entity) {
//        Timetable original = getEntity();
//        copyProperties(original,entity);
//
//        app.getRepository(Timetable.class).update(original,app.getApplicationModel());
//    }
//
//    @Override
//    public Timetable getEntity() {
//        return (Timetable)app.getRepository(Timetable.class).findOne(id);
//    }
//
//    @Override
//    public String redirectTo() {
//        return super.redirectTo(TimetablesResource.class);
//    }
//}