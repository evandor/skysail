package io.skysail.server.app.mxgraph.poc.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.mxgraph.poc.MxGraphPocApplication;
import io.skysail.server.app.mxgraph.poc.Workflow;
import io.skysail.server.restlet.resources.PostEntityServerResource;

public class PostWorkflowResource extends PostEntityServerResource<Workflow> {

    private String id;
    private MxGraphPocApplication app;

    public PostWorkflowResource() {
        addToContext(ResourceContextId.LINK_TITLE, "details");
        addToContext(ResourceContextId.LINK_GLYPH, "search");
    }

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (MxGraphPocApplication) getApplication();
    }


    @Override
    public Workflow getEntity() {
        return new Workflow();//(Workflow)app.getRepository().findOne(id);
    }

	@Override
    public List<Link> getLinks() {
        return super.getLinks(PutBookmarkResource.class);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(BookmarksResource.class);
    }

	@Override
	public Workflow createEntityTemplate() {
		// TODO Auto-generated method stub
		return new Workflow();
	}

	@Override
	public void addEntity(Workflow entity) {
		// TODO Auto-generated method stub

	}


}