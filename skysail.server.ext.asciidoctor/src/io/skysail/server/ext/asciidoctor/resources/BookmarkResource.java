package io.skysail.server.ext.asciidoctor.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ResourceContextId;
import io.skysail.server.ext.asciidoctor.AsciiDocApplication;
import io.skysail.server.ext.asciidoctor.Bookmark;
import io.skysail.server.restlet.resources.EntityServerResource;

public class BookmarkResource extends EntityServerResource<Bookmark> {

    private String id;
    private AsciiDocApplication app;

    public BookmarkResource() {
        addToContext(ResourceContextId.LINK_TITLE, "details");
        addToContext(ResourceContextId.LINK_GLYPH, "search");
    }

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (AsciiDocApplication) getApplication();
    }


    @Override
    public SkysailResponse<Bookmark> eraseEntity() {
        //repository.delete(id);
        return new SkysailResponse<>();
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