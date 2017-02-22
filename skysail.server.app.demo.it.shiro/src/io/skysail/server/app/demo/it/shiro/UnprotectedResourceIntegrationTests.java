package io.skysail.server.app.demo.it.shiro;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.restlet.representation.Representation;

import io.skysail.client.testsupport.BrowserTests;
import io.skysail.client.testsupport.authentication.ShiroAuthenticationStrategy;
import io.skysail.server.app.demo.Time;

public class UnprotectedResourceIntegrationTests extends BrowserTests<UnprotectedResourceBrowser, Time> {

    @Before
    public void setUp() {
        browser = new UnprotectedResourceBrowser(determinePort());
        browser.setAuthenticationStrategy(new ShiroAuthenticationStrategy());
    }

    @Test
    public void read_entity_unauthenticated() throws IOException  { // NOSONAR
        Representation html = browser.getEntities();
        assertTrue(html != null);
    }


}
