package io.skysail.server.app.webconsole;

import java.util.List;

import org.apache.felix.scr.annotations.Deactivate;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
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
import io.skysail.server.menus.MenuItemProvider;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
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
		BundleContext bundleContext = componentContext.getBundleContext();
//		try {
//			Class activatorClass = bundleContext.getBundle().loadClass(STATUS_ACTIVATOR);
//			this.statusActivator = (BundleActivator) activatorClass.newInstance();
//			if (this.statusActivator != null) {
//				this.statusActivator.start(bundleContext);
//			}
//		} catch (Throwable t) {
//			System.out.println(t);
//		}
		//bipReg = new ServicesUsedInfoProvider( bundleContext.getBundle() ).register( bundleContext );
		
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
		
		router.attach(createStaticDirectory());
	}

	@Override
	public EventAdmin getEventAdmin() {
		return eventAdmin;
	}
	
	public List<BundleDescriptor> getBundles() {
		return bundlesTracker.getBundles();
	}

}