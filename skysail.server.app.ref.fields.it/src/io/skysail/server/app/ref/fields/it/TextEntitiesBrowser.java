package io.skysail.server.app.ref.fields.it;

import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.representation.Representation;

import io.skysail.api.links.LinkRelation;
import io.skysail.client.testsupport.ApplicationBrowser;
import io.skysail.client.testsupport.ApplicationClient;
import io.skysail.server.app.ref.fields.FieldsDemoApplication;
import io.skysail.server.app.ref.fields.domain.TextEntity;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TextEntitiesBrowser extends ApplicationBrowser<TextEntitiesBrowser, TextEntity> {

    public TextEntitiesBrowser(MediaType requestMediaType, int port) {
        super(FieldsDemoApplication.APP_NAME, requestMediaType, port);
    }

    @Override
    public TextEntitiesBrowser loginAs(String username, String password) {
        super.loginAs(username, password);
        return this;
    }

    @Override
    protected Form createForm(TextEntity entity) {
        Form form = new Form();
        form.add("astring", entity.getAstring());
        return form;
    }

    public TextEntity createRandomEntity() {
        TextEntity entity = new TextEntity();
        //entity.setName("Bookmark_" + new BigInteger(130, random).toString(32));
        return entity;
    }

    public void create() {
        create(createRandomEntity());
    }

    public void create(TextEntity entity) {
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

    public void updateEntity(TextEntity entity) {
        log.info("{}updating  #{}", ApplicationClient.TESTTAG, entity.getId());
        login();
        updateEntity(client, entity);
    }

    private void deleteEntity(ApplicationClient<?> client, String id) {
        client.gotoAppRoot() //
                .followLinkTitleAndRefId("update", id).followLink(Method.DELETE, null);
    }

    private void createEntity(ApplicationClient<TextEntity> client, TextEntity entity) {
        navigateToPostEntityPage(client);
        client.post(createForm(entity));
        setId(client.getLocation().getLastSegment(true));
    }

    private void navigateToPostEntityPage(ApplicationClient<TextEntity> client) {
        client.gotoAppRoot().followLinkRelation(LinkRelation.CREATE_FORM);
    }

    private void getEntities(ApplicationClient<TextEntity> client) {
        client.gotoAppRoot();
    }

    private void getEntity(ApplicationClient<?> client, String id) {
        client.gotoRoot().followLinkTitle(FieldsDemoApplication.APP_NAME);
    }

    private void updateEntity(ApplicationClient<TextEntity> client, TextEntity entity) {
        client.gotoAppRoot().followLinkTitleAndRefId("update", entity.getId()).followLink(Method.PUT, entity);
    }

}
