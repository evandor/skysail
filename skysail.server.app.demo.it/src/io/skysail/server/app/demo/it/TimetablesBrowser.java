package io.skysail.server.app.demo.it;

import java.math.BigInteger;

import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.representation.Representation;

import ch.qos.logback.core.net.LoginAuthenticator;
import io.skysail.client.testsupport.ApplicationBrowser;
import io.skysail.client.testsupport.ApplicationClient;
import io.skysail.server.app.demo.DemoApplication;
import io.skysail.server.app.demo.Timetable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimetablesBrowser extends ApplicationBrowser<TimetablesBrowser, Timetable> {

    public TimetablesBrowser(MediaType mediaType, int port) {
        super(DemoApplication.APP_NAME, mediaType, port);
    }

    protected Form createForm(Timetable application) {
        Form form = new Form();
        form.add("name", application.getName());
        return form;
    }

    public Timetable createRandomEntity() {
        Timetable timetable = new Timetable();//"App_" + new BigInteger(130, random).toString(32), "pkgName", "../", "projectName");
        timetable.setName("Timetable_" + new BigInteger(130, random).toString(32));
        return timetable;
    }

    public void create() {
        create(createRandomEntity());
    }

    public void create(Timetable application) {
        log.info("{}creating new DbApplication {}", ApplicationClient.TESTTAG, application);
        httpBasiclogin();
        createApplication(client, application);
    }

    public Representation getApplications() {
        log.info("{}getting Applications", ApplicationClient.TESTTAG);
        httpBasiclogin();
        getApplications(client);
        return client.getCurrentRepresentation();
    }

    public void deleteApplication(String id) {
        log.info("{}deleting DbApplication #{}", ApplicationClient.TESTTAG, id);
        login();
        deleteApplication(client, id);
    }

    public Representation getApplication(String id) {
        log.info("{}retrieving DbApplication #{}", ApplicationClient.TESTTAG, id);
        login();
        getApplication(client, id);
        return client.getCurrentRepresentation();
    }

    public void updateApplication(Timetable entity) {
        log.info("{}updating DbApplication #{}", ApplicationClient.TESTTAG, entity.getId());
        login();
        updateApplication(client, entity);
    }

    private void deleteApplication(ApplicationClient<?> client, String id) {
        client.gotoAppRoot() //
                .followLinkTitleAndRefId("update", id).followLink(Method.DELETE, null);
    }

    private void createApplication(ApplicationClient<Timetable> client, Timetable Application) {
        navigateToPostApplication(client);
        client.post(createForm(Application));
        setId(client.getLocation().getLastSegment(true));
    }

    private void navigateToPostApplication(ApplicationClient<Timetable> client) {
        client.gotoAppRoot()
        // .followLinkTitleAndRefId("List of Applications", listId)
        // .followLinkRelation(LinkRelation.CREATE_FORM);
                .followLinkTitle("Create new");
    }

    private void getApplications(ApplicationClient<Timetable> client) {
        client.gotoAppRoot();// .followLinkTitleAndRefId("List of Applications",
                             // id);
    }

    private void getApplication(ApplicationClient<?> client, String id) {
        client.gotoRoot().followLinkTitle(DemoApplication.APP_NAME);
    }

    private void updateApplication(ApplicationClient<Timetable> client, Timetable entity) {
        client.gotoAppRoot().followLinkTitleAndRefId("update", entity.getId()).followLink(Method.PUT, entity);
    }

}
