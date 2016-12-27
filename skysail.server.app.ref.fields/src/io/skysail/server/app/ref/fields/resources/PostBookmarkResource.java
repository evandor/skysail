package io.skysail.server.app.ref.fields.resources;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.restlet.resource.ResourceException;

import io.skysail.server.ResourceContextId;
import io.skysail.server.app.ref.fields.EntityWithTabs;
import io.skysail.server.app.ref.fields.FieldsDemoApplication;
import io.skysail.server.restlet.resources.PostEntityServerResource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PostBookmarkResource extends PostEntityServerResource<EntityWithTabs> {

	protected FieldsDemoApplication app;

    public PostBookmarkResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (FieldsDemoApplication) getApplication();
    }

    @Override
    public EntityWithTabs createEntityTemplate() {
        return new EntityWithTabs();
    }

    @Override
    public void addEntity(EntityWithTabs entity) {
       	entity = new EntityWithTabs();
        String id = app.getRepo().save(entity, app.getApplicationModel()).toString();
        entity.setId(id);

    }

    @Override
    public String redirectTo() {
        return super.redirectTo(BookmarksResource.class);
    }


}