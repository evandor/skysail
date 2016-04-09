package io.skysail.server.app.demo.resources.test;

import org.junit.Before;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.restlet.Context;

import io.skysail.server.app.demo.DemoApplication;
import io.skysail.server.app.demo.DemoRepository;
import io.skysail.server.app.demo.resources.PostTimetableResource;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.testsupport.ResourceTestBase;
import lombok.NonNull;

public abstract class AbstractTimetableResourceTest extends ResourceTestBase {

    @Spy
    protected PostTimetableResource postResource;
//    @Spy
//    protected PutApplicationResource putApplicationResource;
//    @Spy
//    protected ApplicationsResource applicationsResource;
//    @Spy
//    protected ApplicationResource applicationResource;

    @Spy
    protected DemoApplication application;

    protected DemoRepository repo;

    @Before
    public void setUp() throws Exception {
        super.setUpFixture();

        Context context = super.setUpApplication(application);

        setUpRepository(new DemoRepository());

//        super.setUpResource(applicationResource, context);
//        super.setUpResource(applicationsResource, context);
//        super.setUpResource(putApplicationResource, context);
        super.setUpResource(postResource, context);

       // setUpSubject("admin");
        
//        new UniqueNameValidator().setDbService(testDb);
//        new UniqueNameForParentValidator().setDbService(testDb);

    }

    public void setUpRepository(DemoRepository designerRepository) {
        repo = designerRepository;
//        repo.setDbService(testDb);
        repo.activate();
//        ((DemoApplication) application).setDesignerRepository(repo);
        Mockito.when(((DemoApplication) application).getRepository()).thenReturn(repo);
    }

//    public void setUpSubject(String owner) {
//        Mockito.when(subjectUnderTest.getPrincipal()).thenReturn(owner);
//        Mockito.when(subjectUnderTest.getPrincipals()).thenReturn(new SimplePrincipalMap(new HashMap<>()));
//        setSubject(subjectUnderTest);
//    }

    protected void init(SkysailServerResource<?> resource) {
        resource.init(null, request, responses.get(resource.getClass().getName()));
    }

    /**
     * adds provided key/value pair to requests attributes map, removing '#' from the value.
     */
    protected void addAttribute(@NonNull String key, @NonNull String value) {
        getAttributes().put(key, value.replace("#",""));
    }
    
    /**
     * clears the requests attribute map, and adds the provided key/value pair, removing '#' from the value.
     */
    protected void setAttributes(@NonNull String key, @NonNull String value) {
        getAttributes().clear();
        addAttribute(key, value);
    }

//    protected DbApplication createValidApplication() {
//        DbApplication app = DbApplication.builder().name("app_name_" + randomString())
//                .packageName("app_packageName_" + randomString()).path("../").projectName("projectName").build();
//        SkysailResponse<DbApplication> post = postApplicationResource.post(app, JSON_VARIANT);
//        getAttributes().clear();
//
//        return post.getEntity();
//    }

}
