package io.skysail.server.testsupport;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.ClientInfo;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.engine.resource.VariantInfo;
import org.restlet.representation.Variant;
import org.restlet.resource.Resource;

import de.twenty11.skysail.server.um.domain.SkysailUser;
import io.skysail.api.validation.DefaultValidationImpl;
import io.skysail.api.validation.ValidatorService;
import io.skysail.server.app.ServiceList;
import io.skysail.server.app.ServiceListProvider;
import io.skysail.server.app.SkysailApplication;
import lombok.Getter;

/**
 * Base class for resource class testing with a (real) in-memory database.
 *
 * Requests, various services and the userManager are mocked.
 *
 */
//@RunWith(MockitoJUnitRunner.class)
public class ResourceTestBase {

    protected static final String ADMIN_DEFAUTL_PASSWORD = "$2a$12$52R8v2QH3vQRz8NcdtOm5.HhE5tFPZ0T/.MpfUa9rBzOugK.btAHS";

    protected static final Variant HTML_VARIANT = new VariantInfo(MediaType.TEXT_HTML);
    protected static final Variant JSON_VARIANT = new VariantInfo(MediaType.APPLICATION_JSON);

    //	private final BundleContext context = FrameworkUtil.getBundle(ExampleIntegrationTest.class).getBundleContext();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

   // protected static ThreadState subjectThreadState;

    @Getter
    private ConcurrentMap<String, Object> attributes;

    protected Request request;
    @Deprecated // use FormBuilder
    protected Form form;
    protected ClientInfo clientInfo;
    protected Form query;
   // public Subject subjectUnderTest;
    protected SkysailUser adminUser;

    public SkysailApplication application;

    public static TestDb testDb;

    protected ValidatorService validatorService;
    protected ServiceList serviceListProvider;

    protected Map<String, Response> responses = new HashMap<>();

    @BeforeClass
    public static void initDb() {
        testDb = new TestDb();
        //testDb.startDb();
        //testDb.activate();

        Locale englishLocale = Locale.ENGLISH;
        Locale.setDefault(englishLocale);
    }

    public void setUpFixture() {
        attributes = new ConcurrentHashMap<String, Object>();
        request = Mockito.mock(Request.class);
        Mockito.when(request.getAttributes()).thenReturn(attributes);
        Reference resourceRef = Mockito.mock(Reference.class);
        Reference targetRef = Mockito.mock(Reference.class);
        Reference baseRef = Mockito.mock(Reference.class);
        Mockito.when(resourceRef.getBaseRef()).thenReturn(baseRef);
        Mockito.when(baseRef.getTargetRef()).thenReturn(targetRef);
        Mockito.when(request.getResourceRef()).thenReturn(resourceRef);

        clientInfo = new ClientInfo();
        Mockito.when(request.getClientInfo()).thenReturn(clientInfo);

        adminUser = new SkysailUser("admin", ADMIN_DEFAUTL_PASSWORD, "#1");

        form = new Form();
        query = new Form();

//        subjectUnderTest = Mockito.mock(Subject.class);
//        Mockito.when(subjectUnderTest.isAuthenticated()).thenReturn(true);

    }

    public Context setUpApplication(SkysailApplication app) {
        this.application = app;

        ServiceListProvider service = Mockito.mock(ServiceListProvider.class);
        app.setServiceListProvider(service);

        ValidatorService validatorService = new DefaultValidationImpl();
        

        //Mockito.doReturn(validatorService).when(app).getValidatorService();

     //   Mockito.doReturn(Collections.emptySet()).when(app).startPerformanceMonitoring(Mockito.anyString());

        app.setContext(new Context());

        app.createInboundRoot();

        return app.getContext();
    }

    public void setUpResource(Resource resource, Context context) throws Exception {
        Mockito.doReturn(application).when(resource).getApplication();
        Mockito.doReturn(query).when(resource).getQuery();

        Response response = new Response(request);
        responses.put(resource.getClass().getName(), response);
        resource.init(context, request, response);
    }

    
    protected void clearAttributes() {
        getAttributes().clear();
    }

    @After
    public void tearDownSubject() {
        clearSubject();
    }

    @AfterClass
    public static void tearDownShiro() {
 //       doClearSubject();
//        try {
//            org.apache.shiro.mgt.SecurityManager securityManager = getSecurityManager();
//            LifecycleUtils.destroy(securityManager);
//        } catch (UnavailableSecurityManagerException e) {
//            // NOSONAR no problem
//        }
//        setSecurityManager(null);
    }

    /**
     * Allows subclasses to set the currently executing {@link Subject}
     * instance.
     *
     * @param subject
     *            the Subject instance
     */
//    public void setSubject(Subject subject) {
//        clearSubject();
//        subjectThreadState = createThreadState(subject);
//        subjectThreadState.bind();
//    }

//    protected Subject getSubject() {
//        return SecurityUtils.getSubject();
//    }
//
//    protected ThreadState createThreadState(Subject subject) {
//        return new SubjectThreadState(subject);
//    }

    /**
     * Clears Shiro's thread state, ensuring the thread remains clean for future
     * test execution.
     */
    protected void clearSubject() {
       // doClearSubject();
    }

//    private static void doClearSubject() {
//        if (subjectThreadState != null) {
//            subjectThreadState.clear();
//            subjectThreadState = null;
//        }
//    }
//
//    protected static void setSecurityManager(org.apache.shiro.mgt.SecurityManager securityManager) {
//        SecurityUtils.setSecurityManager(securityManager);
//    }
//
//    protected static org.apache.shiro.mgt.SecurityManager getSecurityManager() {
//        return SecurityUtils.getSecurityManager();
//    }

    protected String randomString() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }

//    public void assertSingleValidationFailure(SkysailServerResource<?> resource, SkysailResponse<?> response, String path, String msg) {
//        ConstraintViolationsResponse<?> skysailReponse = (ConstraintViolationsResponse<?>) response;
//        assertThat(responses.get(resource.getClass().getName()).getStatus(), is(equalTo(Status.CLIENT_ERROR_BAD_REQUEST)));
//        assertThat(responses.get(resource.getClass().getName()).getHeaders().getFirst("X-Status-Reason").getValue(), is(equalTo("Validation failed")));
//        assertThat(skysailReponse.getViolations().size(), is(1));
//        ConstraintViolationDetails violation = ((ConstraintViolationsResponse<?>) response).getViolations().iterator()
//                .next();
//        assertThat(violation.getPropertyPath(), containsString(path));
//        assertThat(violation.getMessage(), is(containsString(msg)));
//    }
//
//    protected void assertValidationFailure(SkysailServerResource<?> resource, SkysailResponse<?> response) {
//        ConstraintViolationsResponse<?> skysailReponse = (ConstraintViolationsResponse<?>) response;
//        assertThat(responses.get(resource.getClass().getName()).getStatus(), is(equalTo(Status.CLIENT_ERROR_BAD_REQUEST)));
//        assertThat(responses.get(resource.getClass().getName()).getHeaders().getFirst("X-Status-Reason").getValue(), is(equalTo("Validation failed")));
//        assertThat(skysailReponse.getViolations().size(), greaterThanOrEqualTo(1));
//    }

}
