package io.skysail.server.services;

import java.util.Map;

public interface StringTemplateProvider {

	public Map<String, String> getTemplates();
	
	public String getNamespace();
	
}
