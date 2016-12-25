package io.skysail.server.services;

import java.util.Collections;
import java.util.Map;

import lombok.Getter;

public class DefaultStringTemplateProvider implements StringTemplateProvider {

	@Getter
	private String namespace = "default";
	
	@Override
	public Map<String, String> getTemplates() {
		return Collections.emptyMap();
	}

	@Override
	public String getShortName() {
		return "short";
	}

}
