package io.skysail.server.app.ref.singleentity.it;

import static org.junit.Assert.*;

import java.io.IOException;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.restlet.data.MediaType;

import io.skysail.client.testsupport.BrowserTests2;

public class AccountsCrudIntegrationTestsBase extends BrowserTests2<AccountsBrowser> {

    protected JSONObject entityAsJson;

    @Before
    public void setUp() {
        browser = new AccountsBrowser(MediaType.APPLICATION_JSON, determinePort());
        browser.setUser("admin");
        entityAsJson = browser.createRandomEntity();
    }

    @Test(expected = IllegalStateException.class)
    @Ignore
    public void cannot_create_entity_if_not_logged_in() { // NOSONAR
        browser // no login here!
            .create(entityAsJson);
    }

    @Test
    public void create_and_read_entity() throws IOException  { // NOSONAR
        browser
            .loginAs("admin", "skysail")
            .create(entityAsJson);
        String html = browser.getEntities().getText();
        System.out.println(html);
        assertFalse(html.contains("\"created\":null"));
        assertTrue(html.contains("\"name\":\"account_"));
    }
    
    @Test
    @Ignore
	public void testName() throws Exception {
    	for(int i = 0; i<100; i++) {
    		create_and_read_entity();
    	}
	}
    
    @Test
    public void passed_created_date_is_not_taken_into_account() throws Exception  { // NOSONAR
        entityAsJson.put("created", "12-10-2010 11:11:11");
        browser
            .loginAs("admin", "skysail")
            .create(entityAsJson);
        String html = browser.getEntities().getText();
        System.out.println(html);
        assertFalse(html.contains("\"created\":12-10-2010 11:11:11"));
    }

//    @Test
//    @Ignore
//    public void delete_entity() throws IOException { // NOSONAR
//        browser.create(entityAsJson);
//        browser.deleteApplication(browser.getId());
//        assertThat(browser.getEntities().getText(), not(containsString(entityAsJson.getName())));
//    }
//
//    @Test
//    @Ignore
//    public void update_entity() throws IOException { // NOSONAR
//        browser.create(entityAsJson);
//        assertThat(browser.getEntity(browser.getId()).getText(), containsString(entityAsJson.getName()));
//
//        entityAsJson.setId(browser.getId());
//        entityAsJson.setName(entityAsJson.getName() + "_changed");
//        browser.updateEntity(entityAsJson);
//
//        String updatedText = browser.getEntity(browser.getId()).getText();
//        assertThat(updatedText, containsString("_changed"));
//    }
//
//    @Test
//    @Ignore // not working yet...
//    public void stopping_and_starting_the_ServerBundle_doesnt_break_list_creationg() throws IOException, BundleException {
//        stopAndStartBundle(SkysailServerResource.class);
//        browser.create(entityAsJson);
//        String html = browser.getEntities().getText();
//        assertThat(html, containsString(entityAsJson.getName()));
//    }

}
