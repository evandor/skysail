package io.skysail.server.ui.semanticui;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

import io.skysail.server.services.AbstractStringTemplateProvider;
import io.skysail.server.services.StringTemplateProvider;
import lombok.Getter;

@Component(immediate = true)
public class SemanticUiTemplatesProvider extends AbstractStringTemplateProvider implements StringTemplateProvider {

	@Getter
	private String namespace = "semanticui";

	@Activate
	public void activate(ComponentContext componentContext) {
		bundle = componentContext.getBundleContext().getBundle();
	}

	@Deactivate
	public void deactivate(ComponentContext componentContext) {
		bundle = null;
	}

	

}
