package io.skysail.server.app.demo.it.httpbasic;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.restlet.representation.Representation;

import io.skysail.client.testsupport.BrowserTests;
import io.skysail.server.app.demo.Time;
import io.skysail.server.app.demo.it.httpbasic.browsers.UnprotectedResourceBrowser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UnprotectedResourceIntegrationTests extends BrowserTests<UnprotectedResourceBrowser, Time> {

    @Before
    public void setUp() {
        Arrays.stream(thisBundle.getBundleContext().getBundles()).forEach(b -> {
            log.info("BUNDLE: " + b.getSymbolicName() + " [" + b.getVersion() + "]");
        });
        browser = new UnprotectedResourceBrowser(determinePort());
    }

    @Test
    public void read_entity_unauthenticated() throws IOException { // NOSONAR
        Representation html = browser.getEntities();
        assertTrue(html != null);
        System.out.println(html.getText());
    }

}
