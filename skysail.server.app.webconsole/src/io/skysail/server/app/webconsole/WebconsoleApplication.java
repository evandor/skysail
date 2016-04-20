package io.skysail.server.app.webconsole;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.felix.scr.annotations.Deactivate;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.event.EventAdmin;

import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import io.skysail.server.app.ApiVersion;
import io.skysail.server.app.ApplicationConfiguration;
import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.webconsole.services.ServiceDescriptor;
import io.skysail.server.app.webconsole.services.ServicesResource;
import io.skysail.server.app.webconsole.utils.BundleContextUtil;
import io.skysail.server.menus.MenuItemProvider;
import lombok.extern.slf4j.Slf4j;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
@Slf4j
public class WebconsoleApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

	private static final String STATUS_ACTIVATOR = "org.apache.felix.inventory.impl.Activator";

	@Reference(cardinality = ReferenceCardinality.OPTIONAL)
	private volatile EventAdmin eventAdmin;

	private BundleActivator statusActivator;

	private OsgiBundleTracker bundlesTracker;

	private ServiceRegistration bipReg;

	public WebconsoleApplication() {
		super("webconsole", new ApiVersion(1));
	}

	@Activate
	@Override
	public void activate(ApplicationConfiguration appConfig, ComponentContext componentContext)
			throws ConfigurationException {
		super.activate(appConfig, componentContext);
		bundlesTracker = new OsgiBundleTracker(componentContext.getBundleContext());
	}
	
	@Deactivate
	public void deactivate(ComponentContext componentContext) {
		super.deactivate(componentContext);
		bundlesTracker.deactivate();
	}

	@Override
	protected void attach() {
		super.attach();
		router.attach(new RouteBuilder("", BundlesResource.class).noAuthenticationNeeded());
		router.attach(new RouteBuilder("/bundles", BundlesResource.class).noAuthenticationNeeded());
		router.attach(new RouteBuilder("/bundles/{id}", BundleResource.class).noAuthenticationNeeded());
		router.attach(new RouteBuilder("/services", ServicesResource.class).noAuthenticationNeeded());
		
		router.attach(createStaticDirectory());
	}

	@Override
	public EventAdmin getEventAdmin() {
		return eventAdmin;
	}
	
	public List<BundleDescriptor> getBundles() {
		return bundlesTracker.getBundleDescriptors();
	}

	public BundleDetails getBundle(String id) {
		return bundlesTracker.getBundleDetails(id);
	}
	
	public List<ServiceDescriptor> getOsgiServices() {
        try {
			ServiceReference<?>[] references = BundleContextUtil.getWorkingBundleContext(this.getBundleContext()).getAllServiceReferences( null, null );
			return Arrays.stream(references).map(s -> new ServiceDescriptor(s)).collect(Collectors.toList());
		} catch (InvalidSyntaxException e) {
			log.error(e.getMessage(),e);
		}
        return Collections.emptyList();
	}

}