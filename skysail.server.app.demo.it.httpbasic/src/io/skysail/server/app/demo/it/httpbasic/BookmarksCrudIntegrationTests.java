package io.skysail.server.app.demo.it.httpbasic;

import org.junit.Before;

import io.skysail.server.app.demo.it.BookmarksBrowser;

/**
 * Integration tests for creating, reading, updating, and deleting Bookmarks.
 *
 */
public class BookmarksCrudIntegrationTests extends io.skysail.server.app.demo.it.BookmarksCrudIntegrationTestsBase {

    @Before
    public void setUp() {
        browser = new BookmarksBrowser( determinePort());
        browser.setUser("admin");
        entity = browser.createRandomEntity();
    }

}
