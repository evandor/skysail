package io.skysail.product.demo.flightrecorder;

import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runners.model.Statement;

import io.skysail.client.testsupport.BrowserTests;
import io.skysail.server.app.demo.Bookmark;
import io.skysail.server.app.demo.it.BookmarksBrowser;

/**
 *
 */
public class BookmarksCrudIntegrationTests extends BrowserTests<BookmarksBrowser, Bookmark> {

    private static class RepeatStatement extends Statement {
        private final Statement statement;
        private final int repeat;

        public RepeatStatement(Statement statement, int repeat) {
            this.statement = statement;
            this.repeat = repeat;
        }

        @Override
        public void evaluate() throws Throwable {
            for (int i = 0; i < repeat; i++) {
                statement.evaluate();
            }
        }

    }

    @Rule
    public TestRule repeatRule = (statement, description) -> {
        Statement result = statement;
        Repeat repeat = description.getAnnotation(Repeat.class);
        if (repeat != null) {
            int times = repeat.value();
            result = new RepeatStatement(statement, times);
        }
        return result;
    };

    @Before
    public void setUp() {
        browser = new BookmarksBrowser(2018);
        browser.setUser("admin");
    }

    @Test
    @Repeat(10)
    public void create_and_read_entity() throws IOException { // NOSONAR
        Bookmark entity = browser.createRandomEntity();
        browser.loginAs("admin", "skysail").create(entity);
        String html = browser.getEntities().getText();
       // assertTrue(html.contains(entity.getName()));
    }

}
