package io.skysail.server.app.ref.fields.it;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;

import io.skysail.client.testsupport.BrowserTests;
import io.skysail.client.testsupport.authentication.ShiroAuthenticationStrategy;
import io.skysail.server.app.ref.fields.domain.TextEntity;

import static org.assertj.core.api.Assertions.*;

public class TextEntityIntegrationTests extends BrowserTests<TextEntitiesBrowser, TextEntity> {

    private TextEntity entity;

    @Before
    public void setUp() {
        browser = new TextEntitiesBrowser(MediaType.APPLICATION_JSON, determinePort());
        browser.setAuthenticationStrategy(new ShiroAuthenticationStrategy());
        browser.setUser("admin");
        entity = browser.createRandomEntity();

    }

    @Test
    public void read_entity_unauthenticated() throws IOException  { 
        Representation json = browser.getEntities();
        assertThat(json.getText()).isEqualTo("[]");
    }

    @Test
    public void create_and_read_entity() throws IOException  { // NOSONAR
        browser
            .loginAs("admin", "skysail")
            .create(entity);
        String html = browser.getEntities().getText();
        assertThat(html).contains(entity.getAstring());
    }
}
