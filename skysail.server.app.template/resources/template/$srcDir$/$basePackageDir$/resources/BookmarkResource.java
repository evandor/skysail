package $basePackageName$.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ResourceContextId;
import $basePackageName$.Bookmark;
import $basePackageName$.TemplateApplication;
import $basePackageName$.TemplateRepository;
import io.skysail.server.restlet.resources.EntityServerResource;

public class BookmarkResource extends EntityServerResource<Bookmark> {

    private String id;
    private TemplateApplication app;

    public BookmarkResource() {
        addToContext(ResourceContextId.LINK_TITLE, "details");
        addToContext(ResourceContextId.LINK_GLYPH, "search");
    }

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (TemplateApplication) getApplication();
    }


    @Override
    public SkysailResponse<?> eraseEntity() {
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
        return super.redirectTo(BookmarksResource.class);
    }


}