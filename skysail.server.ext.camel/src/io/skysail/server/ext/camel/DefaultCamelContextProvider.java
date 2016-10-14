package io.skysail.server.ext.camel;

import org.apache.camel.core.osgi.OsgiDefaultCamelContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;

import io.skysail.server.camel.CamelContextProvider;
import lombok.Getter;

/**
 * A skysail extension providing an OSGi-ready CamelContext ready to be used.
 *
 */
@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class DefaultCamelContextProvider implements CamelContextProvider {

    @Getter
    private OsgiDefaultCamelContext camelContext;

    @Activate
    public void activate(ComponentContext componentContext) {
        camelContext = new OsgiDefaultCamelContext(componentContext.getBundleContext());
    }

    @Deactivate
    public void deactivate(ComponentContext componentContext) {
        camelContext = null;
    }

}