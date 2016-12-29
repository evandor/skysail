package io.skysail.server.app.ref.fields;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.ref.fields.EntityWithoutTabs;
import io.skysail.server.app.ref.fields.FieldsDemoApplication;
import io.skysail.server.restlet.resources.EntityServerResource;

public class EntityWithoutTabsResource extends EntityServerResource<EntityWithoutTabs> {

    private String id;
    private FieldsDemoApplication app;

    public EntityWithoutTabsResource() {
        addToContext(ResourceContextId.LINK_TITLE, "details");
        addToContext(ResourceContextId.LINK_GLYPH, "search");
    }

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (FieldsDemoApplication) getApplication();
    }


    @Override
    public SkysailResponse<?> eraseEntity() {
    	app.getRepo().delete(id);
        return new SkysailResponse<>();
    }

    @Override
    public EntityWithoutTabs getEntity() {
        return app.getEntitiesWoTabsRepo().findOne(id);
    }

	/*@Override
    public List<Link> getLinks() {
        return super.getLinks(PutEntityWithoutTabsResource.class);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(EntitiesWithoutTabsResource.class);
    }*/

}
