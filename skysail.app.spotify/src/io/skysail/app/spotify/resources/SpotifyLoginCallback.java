package io.skysail.app.spotify.resources;

import org.restlet.data.Form;
import org.restlet.data.Header;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.engine.util.Base64;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.util.Series;

import io.skysail.app.spotify.TemplateApplication;
import io.skysail.domain.GenericIdentifiable;
import io.skysail.server.restlet.resources.EntityServerResource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SpotifyLoginCallback extends EntityServerResource<GenericIdentifiable> {

    private String code;
    private String state;
    private TemplateApplication application;

    @Override
    protected void doInit() {
        code = getQueryValue("code");
        state = getQueryValue("state");
        application = (TemplateApplication) getApplication();
    }


    @Override
    public GenericIdentifiable getEntity() {
        String storedState = getResponse().getCookieSettings().getFirstValue(TemplateApplication.SPOTIFY_AUTH_STATE);
        if (state == null || !state.equals(storedState)) {
            log.warn("state does not match when logging in to spotify, redirecting to root");
//            Reference redirectTo = new Reference("/");
//            getResponse().redirectSeeOther(redirectTo);
//            return null;
        }
        getResponse().getCookieSettings().removeAll(TemplateApplication.SPOTIFY_AUTH_STATE);
        getAndSetTokens();
        return null;
    }


    private void getAndSetTokens() {
        StringBuilder sb = new StringBuilder(application.getApiBase() + "/api/token");
        ClientResource cr = new ClientResource(sb.toString());

        Series<Header> headers = (Series<Header>) cr.getRequestAttributes().get("org.restlet.http.headers");
        if (headers == null) {
            headers = new Series<>(Header.class);
        }
        String encoded = Base64.encode((application.getClientId() + ":" + application.getClientSecret()).getBytes(), false);
        headers.set("Authorization", "Basic " + encoded);
        log.info("Authorization set to '{}'", "Basic " + encoded);

        StringBuilder payload = new StringBuilder();
        payload.append("{");
        payload.append("\"code\":\""+code+"\",");
        payload.append("\"redirect_uri\":\""+application.getRedirectUri()+"\",");
        payload.append("\"grant_type\":\"authorization_code\"");
        payload.append("}");
        StringRepresentation stringRep = new StringRepresentation(payload.toString(), MediaType.APPLICATION_JSON);

        cr.setMethod(Method.POST);
        try {
            Form form = new Form();
            form.add("code", code);
            form.add("redirect_uri", application.getRedirectUri());
            form.add("grant_type", "authorization_code");

            Representation repr = form.getWebRepresentation();
            repr.setCharacterSet(null);
            Representation posted = cr.post(repr);
            System.out.println(posted.getText());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



}
