package io.skysail.server.app.metrics.resources;

import java.net.MalformedURLException;
import java.net.URL;

import org.restlet.resource.ResourceException;

import io.skysail.server.ResourceContextId;
import io.skysail.server.app.metrics.Bookmark;
import io.skysail.server.app.metrics.MetricsApplication;
import io.skysail.server.restlet.resources.PostEntityServerResource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PostBookmarkResource extends PostEntityServerResource<Bookmark> {

	protected MetricsApplication app;

    public PostBookmarkResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (MetricsApplication) getApplication();
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
        return super.redirectTo(TimersResource.class);
    }


}