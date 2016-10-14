package io.skysail.server.ext.camel.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.mockito.Mockito;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;

import io.skysail.server.ext.camel.DefaultCamelContextProvider;

public class DefaultCamelContextProviderTest {

    @Test
    public void testLifecycle() {
        DefaultCamelContextProvider provider = new DefaultCamelContextProvider();
        ComponentContext componentContext = Mockito.mock(ComponentContext.class);
        BundleContext bundleContext = Mockito.mock(BundleContext.class);
        Mockito.when(componentContext.getBundleContext()).thenReturn(bundleContext);

        assertThat(provider.getCamelContext(),is(nullValue()));
        provider.activate(componentContext);
        assertThat(provider.getCamelContext(),is(not(nullValue())));
        provider.deactivate(componentContext);
        assertThat(provider.getCamelContext(),is(nullValue()));
    }
}
