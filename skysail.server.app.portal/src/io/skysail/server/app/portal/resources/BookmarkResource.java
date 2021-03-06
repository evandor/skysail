package io.skysail.server.app.portal.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.portal.Bookmark;
import io.skysail.server.app.portal.PortalApplication;
import io.skysail.server.restlet.resources.EntityServerResource;

public class BookmarkResource extends EntityServerResource<Bookmark> {

    private String id;
    private PortalApplication app;

    public BookmarkResource() {
        addToContext(ResourceContextId.LINK_TITLE, "details");
        addToContext(ResourceContextId.LINK_GLYPH, "search");
    }

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (PortalApplication) getApplication();
    }


    @Override
    public SkysailResponse<Bookmark> eraseEntity() {
        app.getRepo().delete(id);
        return new SkysailResponse<>();
    }

    @Override
    public Bookmark getEntity() {
        return app.getRepo().findOne(id);
    }

	@Override
    public List<Link> getLinks() {
        return super.getLinks(PutBookmarkResource.class);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(PagesResource.class);
    }


}