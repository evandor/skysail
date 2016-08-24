package io.skysail.server.app.ref.singleentity.it;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.restlet.data.MediaType;

import io.skysail.client.testsupport.BrowserTests;
import io.skysail.domain.Identifiable;

public class AccountsCrudIntegrationTestsBase extends BrowserTests<AccountsBrowser, Identifiable> {

    protected Identifiable entity;

    @Before
    public void setUp() {
        browser = new AccountsBrowser(MediaType.APPLICATION_JSON, determinePort());
        browser.setUser("admin");
        entity = browser.createRandomEntity();
    }

    @Test(expected = IllegalStateException.class)
    public void cannot_create_entity_if_not_logged_in() throws IOException  { // NOSONAR
        browser // no login here!
            .create(entity);
    }

//    @Test
//    @Ignore
//    public void create_and_read_entity() throws IOException  { // NOSONAR
//        browser
//            .loginAs("admin", "skysail")
//            .create(entity);
//        String html = browser.getEntities().getText();
//        assertTrue(html.contains(entity.getName()));
//    }
//
//    @Test
//    @Ignore
//    public void delete_entity() throws IOException { // NOSONAR
//        browser.create(entity);
//        browser.deleteApplication(browser.getId());
//        assertThat(browser.getEntities().getText(), not(containsString(entity.getName())));
//    }
//
//    @Test
//    @Ignore
//    public void update_entity() throws IOException { // NOSONAR
//        browser.create(entity);
//        assertThat(browser.getEntity(browser.getId()).getText(), containsString(entity.getName()));
//
//        entity.setId(browser.getId());
//        entity.setName(entity.getName() + "_changed");
//        browser.updateEntity(entity);
//
//        String updatedText = browser.getEntity(browser.getId()).getText();
//        assertThat(updatedText, containsString("_changed"));
//    }
//
//    @Test
//    @Ignore // not working yet...
//    public void stopping_and_starting_the_ServerBundle_doesnt_break_list_creationg() throws IOException, BundleException {
//        stopAndStartBundle(SkysailServerResource.class);
//        browser.create(entity);
//        String html = browser.getEntities().getText();
//        assertThat(html, containsString(entity.getName()));
//    }

}
