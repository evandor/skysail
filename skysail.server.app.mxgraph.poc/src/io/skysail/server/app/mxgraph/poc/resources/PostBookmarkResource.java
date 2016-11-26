package io.skysail.server.app.mxgraph.poc.resources;

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
import io.skysail.server.app.mxgraph.poc.Bookmark;
import io.skysail.server.app.mxgraph.poc.MxGraphPocApplication;
import io.skysail.server.restlet.resources.PostEntityServerResource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PostBookmarkResource extends PostEntityServerResource<Bookmark> {

	protected MxGraphPocApplication app;

    public PostBookmarkResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (MxGraphPocApplication) getApplication();
    }

    @Override
    public Bookmark createEntityTemplate() {
        return new Bookmark();
    }

    @Override
    public void addEntity(Bookmark entity) {
    	try {
        	entity = new Bookmark();
			entity.setUrl(new URL("http://www.heise.de"));

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
        String id = app.getRepository(Bookmark.class).save(entity, app.getApplicationModel()).toString();
        entity.setId(id);

    }

    @Override
    public String redirectTo() {
        return super.redirectTo(BookmarksResource.class);
    }


}