package io.skysail.server.app.mermaid.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.app.mermaid.MermaidApplication;
import io.skysail.server.app.mermaid.MermaidDefinition;
import io.skysail.server.restlet.resources.EntityServerResource;

public class MermaidResource extends EntityServerResource<MermaidDefinition> {

	private DbRepository repository;
	private MermaidApplication app;

	@Override
    protected void doInit() {
		app = (MermaidApplication) getApplication();
	    repository = app.getRepository(MermaidDefinition.class);
    }

	@Override
	public SkysailResponse<?> eraseEntity() {
		return new SkysailResponse<>();
	}

	@Override
	public MermaidDefinition getEntity() {
		return (MermaidDefinition) repository.findOne(getAttribute("id"));
	}

}
