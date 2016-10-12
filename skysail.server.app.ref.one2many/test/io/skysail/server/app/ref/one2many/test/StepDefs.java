package io.skysail.server.app.ref.one2many.test;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.ClientInfo;
import org.restlet.data.Reference;
import org.restlet.security.Authenticator;

import io.skysail.api.um.AuthenticationService;
import io.skysail.api.um.AuthorizationService;
import io.skysail.api.validation.DefaultValidationImpl;
import io.skysail.domain.Identifiable;
import io.skysail.server.app.ServiceListProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.ref.one2many.One2ManyApplication;
import io.skysail.server.app.ref.one2many.TodoList;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.testsupport.cucumber.CucumberStepContext;

public class StepDefs {

    public static Matcher<TodoList> validAccountWith(Map<String, String> data, String... keys) {
        return new TypeSafeMatcher<TodoList>() {

            @Override
            public void describeTo(Description desc) {
                desc.appendText("expected result: todolist with non-null id");
                Arrays.stream(keys).forEach(key -> {
                    desc.appendText(", " + key + " = ")
                        .appendValue(data.get(key));
                });
            }

            @Override
            protected boolean matchesSafely(TodoList list) {
                if (list.getId() == null) {
                    return false;
                }
                if (!list.getName().equals(data.get("listname"))) {
                    return false;
                }
                return true;
            }
        };
    }

    @Mock
    protected ServiceListProvider serviceListProvider;

    @Mock
    protected AuthenticationService authenticationService;

    @Mock
    protected AuthorizationService authorizationService;

    @Mock
    protected Authenticator authenticator;

    @Mock
    protected Request request;

    @Mock
    protected Reference resourceRef;

    protected ConcurrentMap<String, Object> requestAttributes;

    protected Context context;

    protected SkysailApplication application;

    protected SkysailServerResource<?> resource;

    protected CucumberStepContext stepContext;

    public void setUp(One2ManyApplication app, CucumberStepContext stepContext) {
        this.stepContext = stepContext;
        MockitoAnnotations.initMocks(this);

        this.application = app;
        Context context = new Context();
        Mockito.when(authenticationService.getApplicationAuthenticator(context)).thenReturn(authenticator);
        Mockito.when(serviceListProvider.getAuthenticationService()).thenReturn(authenticationService);
        Mockito.when(serviceListProvider.getAuthorizationService()).thenReturn(authorizationService);
        Mockito.when(serviceListProvider.getValidatorService()).thenReturn(new DefaultValidationImpl());
        //Mockito.when(serviceListProvider.getMetricsCollector()).thenReturn(new NoOpMetricsCollector());
        requestAttributes = new ConcurrentHashMap<>();
        SkysailApplication.setServiceListProvider(serviceListProvider);
        application.setContext(context);
        application.createInboundRoot();

        org.restlet.Application.setCurrent(application);

        Mockito.when(request.getResourceRef()).thenReturn(resourceRef);
        Mockito.when(request.getAttributes()).thenReturn(requestAttributes);
        Mockito.when(request.getClientInfo()).thenReturn(new ClientInfo());
    }

    protected <T extends SkysailServerResource<?>> T setupResource(T resource) {
        resource.setRequest(request);
        resource.init(context, request, new Response(request));
        return resource;
    }

    protected void prepareRequest(SkysailServerResource<?> resource) {
        Identifiable entity = (Identifiable) stepContext.getLastResponse().getEntity();
        String id = entity.getId().toString();
        requestAttributes.put("id", id.replace("#", ""));
        resource.init(context, request, new Response(request));
   }


}
