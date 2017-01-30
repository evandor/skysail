package io.skysail.server.app.mermaid.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.mermaid.MermaidApplication;
import io.skysail.server.app.mermaid.MermaidDefinition;
import io.skysail.server.app.mermaid.repositories.Repository;
import io.skysail.server.restlet.resources.EntityServerResource;

public class MermaidResource extends EntityServerResource<MermaidDefinition> {

	private Repository repository;

	@Override
    protected void doInit() {
	    repository = ((MermaidApplication) getApplication()).getRepository();
    }

	@Override
	public SkysailResponse<?> eraseEntity() {
		return new SkysailResponse<>();
	}

	@Override
	public MermaidDefinition getEntity() {
		return repository.findOne(getAttribute("id"));
	}

}
