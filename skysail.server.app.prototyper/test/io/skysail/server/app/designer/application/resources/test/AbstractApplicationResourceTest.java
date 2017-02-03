//package io.skysail.server.app.designer.application.resources.test;
//
//import static org.hamcrest.CoreMatchers.is;
//import static org.junit.Assert.assertThat;
//
//import org.apache.shiro.subject.Subject;
//import org.restlet.data.Status;
//
//import io.skysail.api.responses.SkysailResponse;
//import io.skysail.server.app.designer.application.DbApplication;
//import io.skysail.server.app.designer.test.AbstractDesignerResourceTest;
//import io.skysail.server.restlet.resources.SkysailServerResource;
//
//public abstract class AbstractApplicationResourceTest extends AbstractDesignerResourceTest {
//
//	public Subject subjectUnderTest;
//
//    @Deprecated // use next method
//    protected void assertListResult(SkysailServerResource<?> resource, SkysailResponse<DbApplication> result, DbApplication app, Status status) {
//        DbApplication dbApplication = result.getEntity();
//        assertThat(responses.get(resource.getClass().getName()).getStatus(),is(status));
//        assertThat(dbApplication.getName(),is(app.getName()));
//        assertThat(dbApplication.getPackageName(),is(app.getPackageName()));
//        assertThat(dbApplication.getPath(),is(app.getPath()));
//        assertThat(dbApplication.getProjectName(),is(app.getProjectName()));
//        assertThat(dbApplication.getOwner(),is("admin"));
//    }
//
//    protected void assertListResult(SkysailServerResource<?> resource, DbApplication dbApplication, DbApplication app, Status status) {
//        assertThat(responses.get(resource.getClass().getName()).getStatus(),is(status));
//        assertThat(dbApplication.getName(),is(app.getName()));
//        assertThat(dbApplication.getPackageName(),is(app.getPackageName()));
//        assertThat(dbApplication.getPath(),is(app.getPath()));
//        assertThat(dbApplication.getProjectName(),is(app.getProjectName()));
//        assertThat(dbApplication.getOwner(),is("admin"));
//    }
//
//}
