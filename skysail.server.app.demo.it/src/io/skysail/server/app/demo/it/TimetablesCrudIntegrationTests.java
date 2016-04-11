package io.skysail.server.app.demo.it;

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
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;

import io.skysail.client.testsupport.BrowserTests;
import io.skysail.server.app.demo.Timetable;
import io.skysail.server.restlet.resources.SkysailServerResource;

/**
 * Integration tests for creating, reading, updating, and deleting Applications.
 *
 */
public class TimetablesCrudIntegrationTests extends BrowserTests<TimetablesBrowser, Timetable> {

    private Timetable entity;

    @Before
    public void setUp() {
        browser = new TimetablesBrowser(MediaType.APPLICATION_JSON, determinePort());
        browser.setUser("admin");
        entity = browser.createRandomEntity();
    }

    @Test  
    public void create_and_read_entity() throws IOException  { // NOSONAR
        browser.create(entity);
        String html = browser.getApplications().getText();
        //assertThat(html, containsString(entity.getName()));
        assertTrue(html.contains(entity.getName()));
    }

    @Test // delete
    @Ignore
    public void new_application_can_be_deleted() throws Exception {
        browser.create(entity);
        browser.deleteApplication(browser.getId());
        assertThat(browser.getApplications().getText(), not(containsString(entity.getName())));
    }

    @Test // update
    @Ignore
    public void altering_application_updates_it_in_DB() throws Exception {
        browser.create(entity);
        assertThat(browser.getApplication(browser.getId()).getText(), containsString(entity.getName()));

        entity.setId(browser.getId());
        //entity.setDesc("description changed");
        entity.setName(entity.getName() + "_changed");
        browser.updateApplication(entity);

        String updatedText = browser.getApplication(browser.getId()).getText();
        assertThat(updatedText, containsString("_changed"));
    }

    @Test
    @Ignore // not working yet...
    public void stopping_and_starting_the_ServerBundle_doesnt_break_list_creationg() throws IOException, BundleException {
        stopAndStartBundle(SkysailServerResource.class);
        browser.create(entity);
        String html = browser.getApplications().getText();
        assertThat(html, containsString(entity.getName()));
    }



    @Test
    @Ignore
    public void posting_new_application_with_name_and_path_persists_it() throws Exception {
        Timetable application = new Timetable();//"app1", "pkgName", "../", "projectName");
       // application.setPath(".");
        browser.create(application);
        Representation applications = browser.getApplications();
        assertThat(applications.getText(), containsString("app1"));
    }

    @Test
    @Ignore
    public void posting_new_application_with_name_containing_specialChar_yields_validation_violation() throws Exception {
        thrown.expect(ResourceException.class);
        thrown.expectMessage("Bad Request");
        browser.create(new Timetable());//"app1!", "pkgName", "../", "projectName"));
    }

}
