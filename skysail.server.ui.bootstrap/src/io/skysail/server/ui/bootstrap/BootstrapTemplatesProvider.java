package io.skysail.server.ui.bootstrap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

import io.skysail.server.services.StringTemplateProvider;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

//@Component(immediate = true)
@Slf4j
public class BootstrapTemplatesProvider implements StringTemplateProvider {

	private Bundle bundle;

	private Map<String, String> templates;

	@Getter
	private String namespace = "boostrap";

	@Activate
	public void activate(ComponentContext componentContext) {
		bundle = componentContext.getBundleContext().getBundle();
	}

	@Deactivate
	public void deactivate(ComponentContext componentContext) {
	}

	@Override
	public synchronized Map<String, String> getTemplates() {
		if (templates != null) {
			return templates;
		}
		try {
			Enumeration<URL> resources = bundle.getResources("/templates");
			while (resources.hasMoreElements()) {
				URL url = (URL) resources.nextElement();
				System.out.println(url);
				addToTemplates(url);
			}
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			templates = Collections.emptyMap();
		}
		return templates;
	}

	private void addToTemplates(URL url) {
		InputStream inputStream;
		try {
			inputStream = url.openConnection().getInputStream();
		    BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
		    StringBuilder content = new StringBuilder();
		    String inputLine;
		    while ((inputLine = in.readLine()) != null) {
		        content.append(inputLine);
		    }
		    in.close();		
		    templates.put(url.toString(), content.toString());
		} catch (IOException e) {
			log.error(e.getMessage(),e);
		}
	}

}
