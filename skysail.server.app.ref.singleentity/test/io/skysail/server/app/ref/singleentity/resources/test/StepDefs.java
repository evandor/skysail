package io.skysail.server.app.ref.singleentity.resources.test;

import java.security.Principal;
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
import io.skysail.api.um.AuthenticationService;
import io.skysail.api.um.AuthorizationService;
import io.skysail.api.validation.DefaultValidationImpl;
import io.skysail.core.app.ApplicationConfiguration;
import io.skysail.core.app.SkysailApplication;
import io.skysail.core.resources.SkysailServerResource;
import io.skysail.domain.Entity;
import io.skysail.server.Constants;
import io.skysail.server.app.ServiceListProvider;
import io.skysail.server.app.ref.singleentity.Account;
import io.skysail.server.app.ref.singleentity.SingleEntityApplication;
import io.skysail.server.testsupport.cucumber.CucumberStepContext;

public class StepDefs {

    public static Matcher<Account> validAccountWith(Map<String, String> data, String... keys) {
        return new TypeSafeMatcher<Account>() {

            @Override
            public void describeTo(Description desc) {
                desc.appendText("expected result: account with non-null id, creationDate");
                Arrays.stream(keys).forEach(key -> {
                    desc.appendText(", " + key + " = ")
                        .appendValue(data.get(key));
                });

            }

            @Override
            protected boolean matchesSafely(Account account) {
                if (account.getCreated() == null) {
                    return false;
                }
                if (account.getId() == null) {
                    return false;
                }
                if (data.get("iban") != null) {
                    if (!data.get("iban").equals(account.getIban())) {
                        return false;
                    }
                }
                if (!account.getName().equals(data.get(Account.class.getName() + "|name"))) {
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

    @Mock
    protected Reference targetRef;

    protected ConcurrentMap<String, Object> requestAttributes;

    protected Context context;

    protected SkysailApplication application;

    protected SkysailServerResource<?> resource;

    protected CucumberStepContext stepContext;

    public void setUp(SingleEntityApplication app, CucumberStepContext stepContext) {
        this.stepContext = stepContext;

        MockitoAnnotations.initMocks(this);

        this.application = app;
        Context context = new Context();
        Principal principal = new Principal() {
            @Override
            public String getName() {
                return "admin";
            }
        };
        Mockito.when(authenticationService.getPrincipal(org.mockito.Matchers.any(Request.class))).thenReturn(principal);
        Mockito.when(authenticationService.getApplicationAuthenticator(context)).thenReturn(authenticator);
        Mockito.when(serviceListProvider.getAuthenticationService()).thenReturn(authenticationService);
        Mockito.when(serviceListProvider.getAuthorizationService()).thenReturn(authorizationService);
        Mockito.when(serviceListProvider.getValidatorService()).thenReturn(new DefaultValidationImpl());
        Mockito.when(serviceListProvider.getMetricsCollector()).thenReturn(new NoOpMetricsCollector());
        requestAttributes = new ConcurrentHashMap<>();
        SkysailApplication.setServiceListProvider(serviceListProvider);

        try {
            ApplicationConfiguration appConfig = Mockito.mock(ApplicationConfiguration.class);
            ComponentContext componentContext = Mockito.mock(ComponentContext.class);
            application.activate(appConfig, componentContext);
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }

        application.setContext(context);
        application.createInboundRoot();


        org.restlet.Application.setCurrent(application);

        Mockito.when(resourceRef.getTargetRef()).thenReturn(targetRef);
        Mockito.when(request.getResourceRef()).thenReturn(resourceRef);
        Mockito.when(request.getAttributes()).thenReturn(requestAttributes);
        Mockito.when(request.getClientInfo()).thenReturn(new ClientInfo());
    }

    protected void prepareRequest(SkysailServerResource<?> resource) {
        Entity entity = (Entity) stepContext.getLastResponse().getEntity();
        String id = entity.getId().toString();
        requestAttributes.put("id", id.replace("#", ""));
        resource.init(context, request, new Response(request));
    }

    protected <T extends SkysailServerResource<?>> T setupResource(T resource) {
        resource.setRequest(request);
        resource.init(context, request, new Response(request));
        return resource;
    }

    protected Map<String, String> addEntityClassIdentifier(Map<String, String> data) {
        return data.entrySet().stream().collect(Collectors.<Map.Entry, String, String> toMap(
                e -> Account.class.getName() + Constants.CLASS_FIELD_NAMES_SEPARATOR + e.getKey(), e -> e.getValue().toString()));
    }


}
