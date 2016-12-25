package io.skysail.server.app.esclient.resources;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.skysail.api.links.Link;
import io.skysail.server.app.esclient.ElastisearchClientApplication;
import io.skysail.server.app.esclient.domain.EsMapping;
import io.skysail.server.restlet.resources.ListServerResource;

public class MappingsResource extends ListServerResource<EsMapping> {
	
	private ElastisearchClientApplication app;
	
	private ObjectMapper mapper = new ObjectMapper();


	@Override
	protected void doInit() throws ResourceException {
		app = (ElastisearchClientApplication)getApplication();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<?> getEntity() {
		try {
			String text = new ClientResource("http://localhost:9200/"+getAttribute("id")+"/_mappings").get().getText();
			Map<String, Object> result = mapper.readValue(text, Map.class);
			Map<String, Object> type = (Map<String, Object>) result.get(getAttribute("id"));
			Map<String, Object> mappings = (Map<String, Object>) type.get("mappings");
			System.out.println(mappings);
			return mappings.keySet().stream().map(EsMapping::new).collect(Collectors.toList());
			
		} catch (ResourceException | IOException e) {
			e.printStackTrace();
		}
		
		return Collections.emptyList();
	}
	
	@Override
	public List<Link> getLinks() {
		return super.getLinks(MappingsResource.class);
	}
}
