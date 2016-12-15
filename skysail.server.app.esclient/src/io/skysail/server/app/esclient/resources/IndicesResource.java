package io.skysail.server.app.esclient.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.app.esclient.ElastisearchClientApplication;
import io.skysail.server.app.esclient.domain.EsIndex;
import io.skysail.server.restlet.resources.ListServerResource;

public class IndicesResource extends ListServerResource<EsIndex> {
	
	private ElastisearchClientApplication app;

	public IndicesResource() {
		super(MappingsResource.class);
	}
	
	@Override
	protected void doInit() {
		app = (ElastisearchClientApplication)getApplication();
	}
	
	@Override
	public List<?> getEntity() {
		return app.get("http://localhost:9200/_cat/indices?format=json", EsIndex.class);
	}

	@Override
	public List<Link> getLinks() {
		return super.getLinks(IndicesResource.class);
	}
}
