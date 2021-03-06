package io.skysail.server.app.demo.it;

import java.math.BigInteger;

import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.representation.Representation;

import io.skysail.client.testsupport.ApplicationBrowser;
import io.skysail.client.testsupport.ApplicationClient;
import io.skysail.server.app.demo.Bookmark;
import io.skysail.server.app.demo.DemoApplication;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BookmarksBrowser extends ApplicationBrowser<BookmarksBrowser, Bookmark> {

    public BookmarksBrowser(int port) {
        super(DemoApplication.APP_NAME, port);
    }

    @Override
    public BookmarksBrowser loginAs(String username, String password) {
        super.loginAs(username, password);
        return this;
    }

    @Override
    protected Form createForm(Bookmark entity) {
        Form form = new Form();
        form.add("name", entity.getName());
        return form;
    }

    public Bookmark createRandomEntity() {
        Bookmark entity = new Bookmark();
        entity.setName("Bookmark_" + new BigInteger(130, random).toString(32));
        return entity;
    }

    public void create() {
        create(createRandomEntity());
    }

    public void create(Bookmark entity) {
        log.info("{}creating new AnEntity {}", ApplicationClient.TESTTAG, entity);
        //login();
        createEntity(client, entity);
    }

    public Representation getEntities() {
        log.info("{}getting Entities", ApplicationClient.TESTTAG);
        login();
        getEntities(client);
        return client.getCurrentRepresentation();
    }

    public void deleteApplication(String id) {
        log.info("{}deleting DbApplication #{}", ApplicationClient.TESTTAG, id);
        login();
        deleteEntity(client, id);
    }

    public Representation getEntity(String id) {
        log.info("{}retrieving #{}", ApplicationClient.TESTTAG, id);
        login();
        getEntity(client, id);
        return client.getCurrentRepresentation();
    }

    public void updateEntity(Bookmark entity) {
        log.info("{}updating  #{}", ApplicationClient.TESTTAG, entity.getId());
        login();
        updateEntity(client, entity);
    }

    private void deleteEntity(ApplicationClient<?> client, String id) {
        client.gotoAppRoot() //
                .followLinkTitleAndRefId("update", id).followLink(Method.DELETE, null);
    }

    private void createEntity(ApplicationClient<Bookmark> client, Bookmark entity) {
        navigateToPostEntityPage(client);
        client.post(createForm(entity), MediaType.APPLICATION_JSON);
        setId(client.getLocation().getLastSegment(true));
    }

    private void navigateToPostEntityPage(ApplicationClient<Bookmark> client) {
        client.gotoAppRoot().followLinkTitle("Create new", MediaType.APPLICATION_JSON);
    }

    private void getEntities(ApplicationClient<Bookmark> client) {
        client.gotoAppRoot();
    }

    private void getEntity(ApplicationClient<?> client, String id) {
        client.gotoRoot().followLinkTitle(DemoApplication.APP_NAME, MediaType.APPLICATION_JSON);
    }

    private void updateEntity(ApplicationClient<Bookmark> client, Bookmark entity) {
        client.gotoAppRoot().followLinkTitleAndRefId("update", entity.getId()).followLink(Method.PUT, entity);
    }

}
