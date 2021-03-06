package io.skysail.server.ui.jquerymobile;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

import io.skysail.server.services.AbstractStringTemplateProvider;
import io.skysail.server.services.StringTemplateProvider;
import lombok.Getter;

@Component(immediate = true)
public class JqueryMobileTemplatesProvider extends AbstractStringTemplateProvider implements StringTemplateProvider {

	@Getter
	private String namespace = "jquerymobile";
	
	@Getter
	private String shortName = "jqm";

	@Activate
	public void activate(ComponentContext componentContext) {
		bundle = componentContext.getBundleContext().getBundle();
	}

	@Deactivate
	public void deactivate(ComponentContext componentContext) {
		this.bundle = null;
	}

	
}
