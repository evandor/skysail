package io.skysail.server.app.designer.codegen.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.codegen.Analysis;
import io.skysail.server.restlet.resources.EntityServerResource;

public class AnalysisResource extends EntityServerResource<Analysis> {

	private DesignerApplication app;

	@Override
	protected void doInit() {
		app = (DesignerApplication) getApplication();
	}

	@Override
	public SkysailResponse eraseEntity() {
		return null;
	}

	@Override
	public Analysis getEntity() {
		String appId = getAttribute("id");
		DbApplication dbApp = app.getRepository().findOne(appId);
		Analysis result = new Analysis();

		return result;
	}

	@Override
	public List<Link> getLinks() {
		return super.getLinks(PostCompilationResource.class);
	}

}
