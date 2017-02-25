package io.skysail.app.spotify.resources;

import java.math.BigInteger;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;

import org.restlet.data.Reference;

import io.skysail.app.spotify.SpotifyApplication;
import io.skysail.domain.GenericIdentifiable;
import io.skysail.server.restlet.resources.EntityServerResource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SpotifyLogin extends EntityServerResource<GenericIdentifiable> {

    private SpotifyApplication application;

    @Override
    protected void doInit() {
        application = (SpotifyApplication) getApplication();
    }

    @Override
    public GenericIdentifiable getEntity() {
        URL spotifyLoginUrl;


        SecureRandom random = new SecureRandom();
        String state = new BigInteger(130, random).toString(16).substring(0,16);

        getResponse().getCookieSettings().add(SpotifyApplication.SPOTIFY_AUTH_STATE, state);


        try {
            StringBuilder sb = new StringBuilder(application.getApiBase() + "/authorize?");
            sb.append("response_type=").append("code");
            sb.append("&client_id=").append("1ec49b84a6cf49149073665f3327adb7");
            sb.append("&scope=").append("user-read-private%20user-read-email");
            sb.append("&redirect_uri=" + URLEncoder.encode(application.getSpotifyRedirectUri(), "UTF-8"));
            sb.append("&state=").append(state);
            String query = sb.toString();
            log.info("querying {}", query);
            spotifyLoginUrl = new URL(query);
            Reference redirectTo = new Reference(spotifyLoginUrl);
            getResponse().redirectSeeOther(redirectTo);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }


}
