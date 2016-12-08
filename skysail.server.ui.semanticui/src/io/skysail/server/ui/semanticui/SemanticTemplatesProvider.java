package io.skysail.server.ui.semanticui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

import io.skysail.server.services.StringTemplateProvider;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Component(immediate = true)
@Slf4j
public class SemanticTemplatesProvider implements StringTemplateProvider {

	private static final String TEMPLATES_DIR = "/templates";

	private Bundle bundle;

	private Map<String, String> templates = new HashMap<>();
	
	@Getter
	private String namespace = "semanticui";

	@Activate
	public void activate(ComponentContext componentContext) {
		bundle = componentContext.getBundleContext().getBundle();
	}

	@Deactivate
	public void deactivate(ComponentContext componentContext) {
	}

	@Override
	public synchronized Map<String, String> getTemplates() {
		if (templates.size() > 0) {
			return templates;
		}
		Enumeration<URL> resources = bundle.findEntries(TEMPLATES_DIR, "*.st", true);
		if (resources == null) {
			templates = Collections.emptyMap();
			return templates;
		}
		while (resources.hasMoreElements()) {
			URL url = (URL) resources.nextElement();
			System.out.println(url);
			addToTemplates(url);
		}
		return templates;
	}

	private void addToTemplates(URL url) {
		InputStream inputStream;
		try {
			inputStream = url.openStream();
		    BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
		    StringBuilder content = new StringBuilder();
		    String inputLine;
		    int line = 0;
		    while ((inputLine = in.readLine()) != null) {
		    	if (line == 1) {
		    		content.append("<!-- Template Start: ").append(url).append("-->\n");
		    	}
		        content.append(inputLine).append("\n");
		        line++;
		    }
		    in.close();		
         	templates.put(getIdentifier(url), content.toString().replace(">>", "<!-- Template End: " + url +" -->\n>>\n"));		    	
		} catch (IOException e) {
			log.error(e.getMessage(),e);
		}
	}

	private String getIdentifier(URL url) {
		return url.toString().split(TEMPLATES_DIR)[1];
	}

}
