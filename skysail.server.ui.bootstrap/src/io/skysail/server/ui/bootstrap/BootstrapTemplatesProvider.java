package io.skysail.server.ui.bootstrap;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

import io.skysail.server.services.AbstractStringTemplateProvider;
import io.skysail.server.services.StringTemplateProvider;

@Component(immediate = true)
public class BootstrapTemplatesProvider extends AbstractStringTemplateProvider implements StringTemplateProvider {

	public static final String NAMESPACE = "bootstrap";
	public static final String SHORTNAME = "bst";

	@Override
	public String getNamespace() {
		return NAMESPACE;
	}

	@Override
	public String getShortName() {
		return SHORTNAME;
	}
	@Activate
	public void activate(ComponentContext componentContext) {
		bundle = componentContext.getBundleContext().getBundle();
	}

	@Deactivate
	public void deactivate(ComponentContext componentContext) {
		this.bundle = null;
	}
}
