package io.skysail.server.app.ref.one2many.test;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.ClientInfo;
import org.restlet.data.Reference;
import org.restlet.security.Authenticator;

import io.skysail.api.metrics.NoOpMetricsCollector;
import io.skysail.api.um.AuthenticationMode;
import io.skysail.api.um.AuthenticationService;
import io.skysail.api.um.AuthorizationService;
import io.skysail.api.validation.DefaultValidationImpl;
import io.skysail.core.app.ApplicationConfiguration;
import io.skysail.core.app.ServiceListProvider;
import io.skysail.core.app.SkysailApplication;
import io.skysail.core.resources.SkysailServerResource;
import io.skysail.domain.Entity;
import io.skysail.server.Constants;
import io.skysail.server.app.ref.one2many.One2ManyApplication;
import io.skysail.server.app.ref.one2many.TodoList;
import io.skysail.server.testsupport.cucumber.CucumberStepContext;

public class StepDefs {

    public static Matcher<TodoList> validAccountWith(Map<String, String> data, String... keys) {
        return new TypeSafeMatcher<TodoList>() {

            @Override
            public void describeTo(Description desc) {
                desc.appendText("expected result: todolist with non-null id");
                Arrays.stream(keys).forEach(key -> {
                    desc.appendText(", " + key + " = ").appendValue(data.get(key));
                });
            }

            @Override
            protected boolean matchesSafely(TodoList list) {
                if (list.getId() == null) {
                    return false;
                }
                if (!list.getName().equals(data.get(TodoList.class.getName() + "|listname"))) {
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
        Mockito.when(authenticationService.getApplicationAuthenticator(context, AuthenticationMode.ANONYMOUS)).thenReturn(authenticator);
        Mockito.when(serviceListProvider.getAuthenticationService()).thenReturn(authenticationService);
        Mockito.when(serviceListProvider.getAuthorizationService()).thenReturn(authorizationService);
        Mockito.when(serviceListProvider.getValidatorService()).thenReturn(new DefaultValidationImpl());
        Mockito.when(serviceListProvider.getMetricsCollector()).thenReturn(new NoOpMetricsCollector());
        Mockito.when(serviceListProvider.getMetricsCollector()).thenReturn(new NoOpMetricsCollector());
        requestAttributes = new ConcurrentHashMap<>();
        SkysailApplication.setServiceListProvider(serviceListProvider);

        try {
            ApplicationConfiguration appConfig = Mockito.mock(ApplicationConfiguration.class);
            ComponentContext componentContext = Mockito.mock(ComponentContext.class);
            application.activate(appConfig, componentContext);
        } catch (ConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
        Entity entity = (Entity) stepContext.getLastResponse().getEntity();
        String id = entity.getId().toString();
        requestAttributes.put("id", id.replace("#", ""));
        resource.init(context, request, new Response(request));
    }

    protected Map<String, String> addEntityClassIdentifier(Map<String, String> data) {
        return data.entrySet().stream().collect(Collectors.<Map.Entry, String, String> toMap(
                e -> TodoList.class.getName() + Constants.CLASS_FIELD_NAMES_SEPARATOR + e.getKey(), e -> e.getValue().toString()));
    }

}
