package io.skysail.app.spotify.resources;

import java.net.URLEncoder;

import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.engine.util.Base64;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import io.skysail.app.spotify.SpotifyApplication;
import io.skysail.domain.GenericIdentifiable;
import io.skysail.server.restlet.resources.EntityServerResource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SpotifyLoginCallback extends EntityServerResource<GenericIdentifiable> {

    private String code;
    private String state;
    private SpotifyApplication application;

    @Override
    protected void doInit() {
        code = getQueryValue("code");
        state = getQueryValue("state");
        application = (SpotifyApplication) getApplication();
    }


    @Override
    public GenericIdentifiable getEntity() {
        String storedState = getResponse().getCookieSettings().getFirstValue(SpotifyApplication.SPOTIFY_AUTH_STATE);
        if (state == null || !state.equals(storedState)) {
            log.warn("state does not match when logging in to spotify, redirecting to root");
//            Reference redirectTo = new Reference("/");
//            getResponse().redirectSeeOther(redirectTo);
//            return null;
        }
        getResponse().getCookieSettings().removeAll(SpotifyApplication.SPOTIFY_AUTH_STATE);
        getAndSetTokens();
        return null;
    }


    private void getAndSetTokens() {
        StringBuilder sb = new StringBuilder(application.getApiBase() + "/api/token");
        ClientResource cr = new ClientResource(sb.toString());

        String encoded = Base64.encode((application.getSpotifyClientId() + ":" + application.getSpotifyClientSecret()).getBytes(), false);

        ChallengeResponse challengeResponse = new ChallengeResponse(
                new ChallengeScheme("", ""));
        challengeResponse.setRawValue("Basic " + encoded);
        cr.setChallengeResponse(challengeResponse);

        log.info("Authorization set to '{}'", "Basic " + encoded);


        cr.setMethod(Method.POST);
        try {
            Form form = new Form();
            form.add("code", code);
            form.add("redirect_uri", URLEncoder.encode(application.getSpotifyRedirectUri(), "UTF-8"));
            form.add("grant_type", "authorization_code");
            log.info("form: {}",form);
            Representation repr = form.getWebRepresentation();
            repr.setCharacterSet(null);
            Representation posted = cr.post(repr, MediaType.APPLICATION_JSON);
            System.out.println(posted.getText());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



}
