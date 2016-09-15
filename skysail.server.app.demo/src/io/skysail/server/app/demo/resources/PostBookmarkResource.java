package io.skysail.server.app.demo.resources;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.restlet.resource.ResourceException;

import io.skysail.server.ResourceContextId;
import io.skysail.server.app.demo.Bookmark;
import io.skysail.server.app.demo.DemoApplication;
import io.skysail.server.restlet.resources.PostEntityServerResource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PostBookmarkResource extends PostEntityServerResource<Bookmark> {

	protected DemoApplication app;

    public PostBookmarkResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (DemoApplication) getApplication();
    }

    @Override
    public Bookmark createEntityTemplate() {
        return new Bookmark();
    }

    @Override
    public void addEntity(Bookmark entity) {
        String id = app.getRepository(Bookmark.class).save(entity, app.getApplicationModel()).toString();
        entity.setId(id);

    }

    @Override
    public String redirectTo() {
        return super.redirectTo(BookmarksResource.class);
    }

    private void analyzeBookmarkUrl(Bookmark entity) {
        Document doc;
        try {
            doc = Jsoup.connect(entity.getUrl().toExternalForm()).get();
            if (StringUtils.isEmpty(entity.getName())) {
                entity.setName(doc.title());
            }
            Element e=doc.head().select("link[href~=.*\\.ico]").first();
            if (e != null) {
                entity.setFavicon(e.attr("href"));
            }
            entity.setMetaDescription(getMetaTag(doc, "description"));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    String getMetaTag(Document document, String attr) {
        Elements elements = document.select("meta[name=" + attr + "]");
        for (Element element : elements) {
            final String s = element.attr("content");
            if (s != null)
                return s;
        }
        elements = document.select("meta[property=" + attr + "]");
        for (Element element : elements) {
            final String s = element.attr("content");
            if (s != null)
                return s;
        }
        return null;
    }

}