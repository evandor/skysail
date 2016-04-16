package io.skysail.server.app.demo.it.shiro;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.osgi.framework.BundleException;
import org.restlet.data.MediaType;

import io.skysail.client.testsupport.BrowserTests;
import io.skysail.client.testsupport.authentication.ShiroAuthenticationStrategy;
import io.skysail.server.app.demo.Bookmark;
import io.skysail.server.restlet.resources.SkysailServerResource;

/**
 * Integration tests for creating, reading, updating, and deleting Bookmarks.
 *
 */
public class BookmarksCrudIntegrationTests extends BrowserTests<BookmarksBrowser, Bookmark> {

    private Bookmark entity;

    @Before
    public void setUp() {
        browser = new BookmarksBrowser(MediaType.APPLICATION_JSON, determinePort());
        browser.setAuthenticationStrategy(new ShiroAuthenticationStrategy());
        browser.setUser("admin");
        entity = browser.createRandomEntity();
    }

    @Test  
    public void create_and_read_entity() throws IOException  { // NOSONAR
        browser.create(entity);
        String html = browser.getEntities().getText();
        assertTrue(html.contains(entity.getName()));
    }

    @Test
    @Ignore
    public void delete_entity() throws IOException { // NOSONAR
        browser.create(entity);
        browser.deleteApplication(browser.getId());
        assertThat(browser.getEntities().getText(), not(containsString(entity.getName())));
    }

    @Test
    @Ignore
    public void update_entity() throws IOException { // NOSONAR
        browser.create(entity);
        assertThat(browser.getEntity(browser.getId()).getText(), containsString(entity.getName()));

        entity.setId(browser.getId());
        entity.setName(entity.getName() + "_changed");
        browser.updateEntity(entity);

        String updatedText = browser.getEntity(browser.getId()).getText();
        assertThat(updatedText, containsString("_changed"));
    }

    @Test
    @Ignore // not working yet...
    public void stopping_and_starting_the_ServerBundle_doesnt_break_list_creationg() throws IOException, BundleException {
        stopAndStartBundle(SkysailServerResource.class);
        browser.create(entity);
        String html = browser.getEntities().getText();
        assertThat(html, containsString(entity.getName()));
    }
}
