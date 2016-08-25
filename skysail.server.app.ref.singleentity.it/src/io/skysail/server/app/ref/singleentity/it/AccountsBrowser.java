package io.skysail.server.app.ref.singleentity.it;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;

import io.skysail.client.testsupport.ApplicationBrowser;
import io.skysail.client.testsupport.ApplicationBrowser2;
import io.skysail.client.testsupport.ApplicationClient;
import io.skysail.client.testsupport.ApplicationClient2;
import io.skysail.domain.Identifiable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AccountsBrowser extends ApplicationBrowser2 {

    public AccountsBrowser(MediaType mediaType, int port) {
        super("singleEntityApplication", mediaType, port);
    }

    @Override
    public AccountsBrowser loginAs(String username, String password) {
        super.loginAs(username, password);
        return this;
    }

    @Override
    protected Form createForm(String entity) {
        Form form = new Form();
        form.add("name", "name");// entityAsJson.getName());
        return form;
    }

    public JsonRepresentation createRandomEntity() {
    	JSONObject jo = new JSONObject();
    	try {
			jo.put("name", "name");
			jo.put("iban", "DE00000000000000000000");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return new JsonRepresentation(jo);
    	//return "[{\"name\": \"amount\",\"iban\": \"DE00000000000000000000\"}]";
    }

    public void create() {
        create(createRandomEntity());
    }

    public void create(JsonRepresentation entity) {
        log.info("{}creating new Entity {}", ApplicationClient.TESTTAG, entity);
        //login();
        createEntity(client, entity);
    }

    public Representation getEntities() {
        log.info("{}getting Entities", ApplicationClient.TESTTAG);
        login();
        getEntities(client);
        return client.getCurrentRepresentation();
    }

//    public void deleteApplication(String id) {
//        log.info("{}deleting DbApplication #{}", ApplicationClient.TESTTAG, id);
//        login();
//        deleteEntity(client, id);
//    }
//
//    public Representation getEntity(String id) {
//        log.info("{}retrieving #{}", ApplicationClient.TESTTAG, id);
//        login();
//        getEntity(client, id);
//        return client.getCurrentRepresentation();
//    }
//
//    public void updateEntity(Account entityAsJson) {
//        log.info("{}updating  #{}", ApplicationClient.TESTTAG, entityAsJson.getId());
//        login();
//        updateEntity(client, entityAsJson);
//    }
//
//    private void deleteEntity(ApplicationClient<?> client, String id) {
//        client.gotoAppRoot() //
//                .followLinkTitleAndRefId("update", id).followLink(Method.DELETE, null);
//    }
//
    private void createEntity(ApplicationClient2 client, JsonRepresentation entity) {
        navigateToPostEntityPage(client);
        //client.post(createForm(entity));
        client.post(entity);
        setId(client.getLocation().getLastSegment(true));
    }

    private void navigateToPostEntityPage(ApplicationClient2 client) {
        client.gotoAppRoot().followLinkTitle("create");
    }

    private void getEntities(ApplicationClient2 client) {
        client.gotoAppRoot();
    }

//    private void getEntity(ApplicationClient<?> client, String id) {
//        client.gotoRoot().followLinkTitle(SingleEntityApplication.APP_NAME);
//    }
//
//    private void updateEntity(ApplicationClient<Account> client, Account entityAsJson) {
//        client.gotoAppRoot().followLinkTitleAndRefId("update", entityAsJson.getId()).followLink(Method.PUT, entityAsJson);
//    }

}
