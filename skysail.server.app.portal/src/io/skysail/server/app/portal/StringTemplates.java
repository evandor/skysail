package io.skysail.server.app.portal;

import java.util.HashMap;
import java.util.Map;

import org.osgi.service.component.annotations.Component;

import io.skysail.server.services.StringTemplateProvider;

@Component(immediate = true)
public class StringTemplates implements StringTemplateProvider{

	private static Map<String, String> theMap = new HashMap<>();

	static {
		theMap.put("/index_.st", "index(user, messages, converter, model)::=<<hi>>");
	}
	
	@Override
	public Map<String, String> getTemplates() {
		return theMap;
	}

	@Override
	public String getNamespace() {
		return "portal";
	}

	@Override
	public String getShortName() {
		return "portal";
	}

}
