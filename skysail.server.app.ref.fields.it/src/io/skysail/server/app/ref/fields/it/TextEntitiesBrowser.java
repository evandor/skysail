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

    private static final MediaType DEFAULT_ACCEPTED_MEDIA_TYPE = MediaType.APPLICATION_JSON;

    public TextEntitiesBrowser(int port) {
        super(FieldsDemoApplication.APP_NAME, port);
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

    public Representation getEntities(MediaType acceptedMediaType) {
        log.info("{}getting Entities with Accept Header '{}'", ApplicationClient.TESTTAG, acceptedMediaType);
        login();
        getEntities(client,acceptedMediaType);
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
        client.post(createForm(entity), MediaType.APPLICATION_JSON);
        setId(client.getLocation().getLastSegment(true));
    }

    private void navigateToPostEntityPage(ApplicationClient<TextEntity> client) {
        client.gotoAppRoot().followLinkRelation(LinkRelation.CREATE_FORM);
    }

    private void getEntities(ApplicationClient<TextEntity> client, MediaType mediaType) {
        client.gotoAppRoot(mediaType);
    }

    private void getEntity(ApplicationClient<?> client, String id) {
        client.gotoRoot().followLinkTitle(FieldsDemoApplication.APP_NAME, MediaType.APPLICATION_JSON);
    }

    private void updateEntity(ApplicationClient<TextEntity> client, TextEntity entity) {
        client.gotoAppRoot().followLinkTitleAndRefId("update", entity.getId()).followLink(Method.PUT, entity);
    }

}
