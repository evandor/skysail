package io.skysail.server.app.ref.fields.it;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;

import io.skysail.client.testsupport.BrowserTests;
import io.skysail.client.testsupport.authentication.ShiroAuthenticationStrategy;
import io.skysail.server.app.ref.fields.domain.TextEntity;

public class TextEntityIntegrationTests extends BrowserTests<TextEntitiesBrowser, TextEntity> {

    private static final String ADMIN_USER = "admin";
    private static final String DEFAULT_PASSWORD = "skysail";

    private TextEntity entity;

    @Before
    public void setUp() {
        browser = new TextEntitiesBrowser(determinePort());
        browser.setAuthenticationStrategy(new ShiroAuthenticationStrategy());
        browser.setUser(ADMIN_USER);
        entity = browser.createRandomEntity();
    }

    @Test
    public void createEntity_and_check_result() throws IOException { // NOSONAR
        Representation result = browser.loginAs(ADMIN_USER, DEFAULT_PASSWORD).create(entity);
        assertThat(result.getText()).contains("xxx");
    }

    @Test
    public void createEntity_and_return_listView_asHTML() throws IOException { // NOSONAR
        browser.loginAs(ADMIN_USER, DEFAULT_PASSWORD).create(entity);
        Representation html = browser.getEntities(MediaType.TEXT_HTML);
        assertThat(html.getText()).contains("<td class=\"renderedTableCell\">"+entity.getAstring()+"</td>");
    }

    @Test
    public void createEntity_and_return_listView_asJSON() throws IOException { // NOSONAR
        browser.loginAs(ADMIN_USER, DEFAULT_PASSWORD).create(entity);
        Representation json = browser.getEntities(MediaType.APPLICATION_JSON);
        assertThat(json.getText()).contains("\"astring\":\""+entity.getAstring()+"\"");
    }

    @Test
    public void create_and_read_entity() throws IOException { // NOSONAR
        browser.loginAs(ADMIN_USER, DEFAULT_PASSWORD).create(entity);
        String html = browser.getEntities(MediaType.APPLICATION_JSON).getText();
        assertThat(html).contains(entity.getAstring());
    }
}
