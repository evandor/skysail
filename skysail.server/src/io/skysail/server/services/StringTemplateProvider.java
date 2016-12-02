package io.skysail.server.services;

import java.util.Map;

@FunctionalInterface
public interface StringTemplateProvider {

	public Map<String, String> getTemplates();
	
}
