package io.skysail.server.app.mxgraph.poc.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.mxgraph.poc.Bookmark;
import io.skysail.server.app.mxgraph.poc.MxGraphPocApplication;
import io.skysail.server.restlet.resources.EntityServerResource;

public class BookmarkResource extends EntityServerResource<Bookmark> {

    private String id;
    private MxGraphPocApplication app;

    public BookmarkResource() {
        addToContext(ResourceContextId.LINK_TITLE, "details");
        addToContext(ResourceContextId.LINK_GLYPH, "search");
    }

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (MxGraphPocApplication) getApplication();
    }

    @Override
    public Bookmark getEntity() {
        return (Bookmark)app.getRepository().findOne(id);
    }

	@Override
    public List<Link> getLinks() {
        return super.getLinks(PutBookmarkResource.class);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(BookmarksResource.class);
    }


}