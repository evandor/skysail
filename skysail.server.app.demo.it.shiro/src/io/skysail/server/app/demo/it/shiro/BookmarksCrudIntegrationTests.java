package io.skysail.server.app.demo.it.shiro;

import org.junit.Before;

import io.skysail.client.testsupport.authentication.ShiroAuthenticationStrategy;
import io.skysail.server.app.demo.it.BookmarksBrowser;
import io.skysail.server.app.demo.it.BookmarksCrudIntegrationTestsBase;

/**
 * Integration tests for creating, reading, updating, and deleting Bookmarks.
 *
 */
public class BookmarksCrudIntegrationTests extends BookmarksCrudIntegrationTestsBase {

    @Before
    public void setUp() {
        browser = new BookmarksBrowser(determinePort());
        browser.setAuthenticationStrategy(new ShiroAuthenticationStrategy());
        browser.setUser("admin");
        entity = browser.createRandomEntity();
    }


}
