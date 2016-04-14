package io.skysail.server.app.demo.it;

import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;

import io.skysail.client.testsupport.ApplicationBrowser;
import io.skysail.client.testsupport.ApplicationClient;
import io.skysail.server.app.demo.DemoApplication;
import io.skysail.server.app.demo.Time;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UnprotectedResourceBrowser extends ApplicationBrowser<UnprotectedResourceBrowser, Time> {

    public UnprotectedResourceBrowser(MediaType mediaType, int port) {
        super(DemoApplication.APP_NAME, mediaType, port);
    }

    public Representation getEntities() {
        log.info("{}getting Entities", ApplicationClient.TESTTAG);
        getEntities(client);
        return client.getCurrentRepresentation();
    }

    private void getEntities(ApplicationClient<Time> client) {
    	client.gotoUrl("/demoapp/v1/unprotected/times");
    }

	@Override
	protected Form createForm(Time entity) {
		return null;
	}

}