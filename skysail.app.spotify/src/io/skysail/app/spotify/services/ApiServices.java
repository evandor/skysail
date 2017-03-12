package io.skysail.app.spotify.services;

import java.io.IOException;
import java.security.Principal;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.restlet.Response;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import io.skysail.app.spotify.SpotifyApplication;
import io.skysail.app.spotify.config.SpotifyConfiguration;
import io.skysail.ext.oauth2.OAuth2Proxy;
import lombok.Getter;

@Component(immediate = true, service = ApiServices.class)
public class ApiServices {

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    @Getter
    private SpotifyConfiguration config;

    public String getMe(Principal principal) {
        StringBuilder sb = new StringBuilder("https://api.spotify.com/v1/me");
        ClientResource cr = new ClientResource(sb.toString());

        ChallengeResponse challengeResponse = new ChallengeResponse(new ChallengeScheme("", ""));
        String accessToken = OAuth2Proxy.getAccessToken(principal,SpotifyApplication.APP_NAME).get();
        challengeResponse.setRawValue("Bearer " + accessToken);
        cr.setChallengeResponse(challengeResponse);

        cr.setMethod(Method.GET);
        try {
            Representation posted = cr.get(MediaType.APPLICATION_JSON);
            return posted.getText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }

    public String getPlaylistsAsJson(Principal principal, Response response) {
        StringBuilder sb = new StringBuilder("https://api.spotify.com/v1/me/playlists");
        ClientResource cr = new ClientResource(sb.toString());

        ChallengeResponse challengeResponse = new ChallengeResponse(new ChallengeScheme("", ""));
        String accessToken = OAuth2Proxy.getAccessToken(principal,SpotifyApplication.APP_NAME).get();
        challengeResponse.setRawValue("Bearer " + accessToken);
        cr.setChallengeResponse(challengeResponse);

        cr.setMethod(Method.GET);
        Representation posted = cr.get(MediaType.APPLICATION_JSON);
        try {
            return posted.getText();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
