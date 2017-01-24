package io.skysail.server.app.mermaid.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.mermaid.MermaidApplication;
import io.skysail.server.app.mermaid.MermaidDefinition;
import io.skysail.server.app.mermaid.repositories.MermaidDefinitionsRepo;
import io.skysail.server.restlet.resources.EntityServerResource;

public class MermaidResource extends EntityServerResource<MermaidDefinition> {

	private MermaidDefinitionsRepo repo;

	@Override
    protected void doInit() {
        repo = ((MermaidApplication) getApplication()).getMermaidDefinitionsRepo();
    }

	@Override
	public SkysailResponse<?> eraseEntity() {
		return new SkysailResponse<>();
	}

	@Override
	public MermaidDefinition getEntity() {
		return repo.findOne(getAttribute("id"));
	}

}
