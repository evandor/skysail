package io.skysail.server.converter.impl;

import org.restlet.representation.Variant;
import org.restlet.resource.Resource;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.converter.HtmlConverter;
import io.skysail.server.model.JsonResourceModel;
import io.skysail.server.model.ResourceModel;
import io.skysail.server.restlet.resources.SkysailServerResource;
import lombok.extern.slf4j.Slf4j;

/**
 * Leverages StringTemplate engine to create html for resource representation.
 * 
 * The templates are provided by the resources' bundle or the skysail.server.converter bundle.
 * 
 */
@Slf4j
public class StringTemplateRenderer2 extends StringTemplateRenderer {

	public StringTemplateRenderer2(HtmlConverter htmlConverter, Resource resource) {
		super(htmlConverter, resource);
	}
	
	@Override
	protected ResourceModel<SkysailServerResource<?>, ?> createResourceModel(Object entity, Variant target,
			SkysailServerResource<?> resource) {
		JsonResourceModel<SkysailServerResource<?>, ?> jsonResourceModel = new JsonResourceModel(resource,
				(SkysailResponse<?>) entity, htmlConverter.getUserManagementProvider(), target, theme);
		jsonResourceModel.setMenuItemProviders(menuProviders);
		jsonResourceModel.setFilterParser(filterParser);
		jsonResourceModel.setInstallationProvider(installationProvider);
		jsonResourceModel.setTemplateProvider(htmlConverter.getTemplateProvider());
		jsonResourceModel.process();
		
		return jsonResourceModel;
	}
}
