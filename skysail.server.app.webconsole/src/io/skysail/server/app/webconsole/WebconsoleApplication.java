package io.skysail.server.app.webconsole;

import java.util.List;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.event.EventAdmin;

import io.skysail.server.app.ApiVersion;
import io.skysail.server.app.ApplicationConfiguration;
import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.webconsole.bundles.BundleDescriptor;
import io.skysail.server.app.webconsole.bundles.BundleDetails;
import io.skysail.server.app.webconsole.bundles.BundleResource;
import io.skysail.server.app.webconsole.bundles.BundlesResource;
import io.skysail.server.app.webconsole.osgi.OsgiService;
import io.skysail.server.app.webconsole.services.ServiceDescriptor;
import io.skysail.server.app.webconsole.services.ServiceDetails;
import io.skysail.server.app.webconsole.services.ServiceResource;
import io.skysail.server.app.webconsole.services.ServicesResource;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.restlet.RouteBuilder;
import lombok.Getter;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class WebconsoleApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

	@Reference(cardinality = ReferenceCardinality.OPTIONAL)
	private volatile EventAdmin eventAdmin;
	
	@Reference(cardinality = ReferenceCardinality.MANDATORY)
	@Getter
	private OsgiService osgiService;

	public WebconsoleApplication() {
		super("webconsole", new ApiVersion(1));
	}

	@Activate
	@Override
	public void activate(ApplicationConfiguration appConfig, ComponentContext componentContext)
			throws ConfigurationException {
		super.activate(appConfig, componentContext);
	}
	
	@Deactivate
	@Override
	public void deactivate(ComponentContext componentContext) {
		super.deactivate(componentContext);
	}

	@Override
	protected void attach() {
		super.attach();
		router.attach(new RouteBuilder("", BundlesResource.class).noAuthenticationNeeded());
		router.attach(new RouteBuilder("/bundles", BundlesResource.class).noAuthenticationNeeded());
		router.attach(new RouteBuilder("/bundles/{id}", BundleResource.class).noAuthenticationNeeded());
		router.attach(new RouteBuilder("/services", ServicesResource.class).noAuthenticationNeeded());
		router.attach(new RouteBuilder("/services/{id}", ServiceResource.class).noAuthenticationNeeded());
		
		router.attach(createStaticDirectory());
	}

	@Override
	public EventAdmin getEventAdmin() {
		return eventAdmin;
	}
	
	public List<BundleDescriptor> getBundles() {
		return osgiService.getBundleDescriptors();
	}

	public BundleDetails getBundle(String id) {
		return osgiService.getBundleDetails(id);
	}

	public ServiceDetails getService(String id) {
		return osgiService.getService(id);
	}

	public List<ServiceDescriptor> getOsgiServices() {
		return osgiService.getOsgiServices();
	}
	
	

}