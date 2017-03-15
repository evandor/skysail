package io.skysail.core.app.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.restlet.Context;
import org.restlet.routing.Router;
import org.restlet.routing.VirtualHost;
import org.restlet.service.StatusService;
import org.restlet.util.ServerList;

import io.skysail.core.SkysailComponent;
import io.skysail.core.app.ApplicationList;
import io.skysail.core.app.ApplicationProvider;
import io.skysail.core.app.SkysailApplication;

public class ApplicationListTest {

    private ApplicationList applicationList;
    private ApplicationProvider provider;
    private SkysailApplication application;
    private SkysailComponent skysailComponent;
    private VirtualHost defaultHost;
    private Router internalRouter;
    private ServerList servers;

    @BeforeClass
    public static void setUpBeforeClass() {
    }

    @Before
    public void setUp() {
        applicationList = new ApplicationList();
        provider = mock(ApplicationProvider.class);
        application = mock(SkysailApplication.class);
        when(provider.getApplication()).thenReturn(application);
        when(application.getName()).thenReturn("theAppName");

        Context context = mock(Context.class);
        internalRouter = mock(Router.class);
        defaultHost = mock(VirtualHost.class);

        skysailComponent = mock(SkysailComponent.class);
        servers = new ServerList(context,null);

        when(skysailComponent.getDefaultHost()).thenReturn(defaultHost);
        when(skysailComponent.getInternalRouter()).thenReturn(internalRouter);
        when(skysailComponent.getServers()).thenReturn(servers);
    }

    @Test
    public void adds_first_application() {
        applicationList.addApplicationProvider(provider);
        verify(application, times(1)).setStatusService(any(StatusService.class));
        assertThat(applicationList.getApplications().size(), is(1));
    }

    @Test(expected = IllegalStateException.class)
    public void checks_that_application_is_added_twice() {
        applicationList.addApplicationProvider(provider);
        applicationList.addApplicationProvider(provider);
    }

    @Test
    public void removes_application_again() {
        applicationList.addApplicationProvider(provider);
        applicationList.removeApplicationProvider(provider);
        assertThat(applicationList.getApplications().size(), is(0));
    }

    @Test
    public void attach() {
        applicationList.addApplicationProvider(provider);
        applicationList.attach(skysailComponent);
        verify(defaultHost).attach("/theAppName", application);
    }

    @Test
    public void application_is_not_attached_if_component_is_null() {
        skysailComponent = null;
        applicationList.addApplicationProvider(provider);
        applicationList.attach(skysailComponent);
        verify(defaultHost, times(0)).attach("/theAppName", application);
    }

    @Test
    public void detach() {
        applicationList.addApplicationProvider(provider);
        applicationList.attach(skysailComponent);
        applicationList.detach(skysailComponent);
        verify(defaultHost).detach(application);
        verify(internalRouter).detach(application);
    }

//    @Test
//    public void detachSkysailComponent() {
//
//    }
//
//    @Test
//    public void detachApplicationSkysailComponent() {
//
//    }

}
