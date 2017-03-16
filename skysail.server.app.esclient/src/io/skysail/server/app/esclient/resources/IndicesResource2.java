package io.skysail.server.app.esclient.resources;

import java.util.List;
import java.util.stream.Collectors;

import io.skysail.api.links.Link;
import io.skysail.server.app.esclient.ElastisearchClientApplication;
import io.skysail.server.app.esclient.domain.EsIndex;
import io.skysail.server.restlet.resources.EntityServerResource;

public class IndicesResource2 extends EntityServerResource<EsIndexListHolder> {

	private ElastisearchClientApplication app;

	public IndicesResource2() {
		//super(MappingsResource.class);
	}

	@Override
	protected void doInit() {
		app = (ElastisearchClientApplication)getApplication();
	}

	@Override
	public List<Link> getLinks() {
		return super.getLinks(IndicesResource2.class);
	}

	@Override
	public EsIndexListHolder getEntity() {
		String apiUrl = "http://localhost:9200/_cat/indices?format=json";
		List<EsIndex> indices = app.get(apiUrl, EsIndex.class).stream().map(EsIndex.class::cast).collect(Collectors.toList());
		return new EsIndexListHolder(apiUrl, indices);
	}
}
