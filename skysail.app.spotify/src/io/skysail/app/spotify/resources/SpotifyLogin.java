package io.skysail.app.spotify.resources;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;

import org.restlet.data.Reference;

import io.skysail.app.spotify.TemplateApplication;
import io.skysail.domain.GenericIdentifiable;
import io.skysail.server.restlet.resources.EntityServerResource;

public class SpotifyLogin extends EntityServerResource<GenericIdentifiable> {

    private TemplateApplication application;

    @Override
    protected void doInit() {
        application = (TemplateApplication) getApplication();
    }

    @Override
    public GenericIdentifiable getEntity() {
        URL spotifyLoginUrl;

        SecureRandom random = new SecureRandom();
        String state = new BigInteger(130, random).toString(16).substring(0,16);

        getResponse().getCookieSettings().add(TemplateApplication.SPOTIFY_AUTH_STATE, state);


        try {
            StringBuilder sb = new StringBuilder(application.getApiBase() + "/authorize?");
            sb.append("response_type=").append("code");
            sb.append("&client_id=").append("1ec49b84a6cf49149073665f3327adb7");
            sb.append("&scope=").append("user-read-private user-read-email");
            sb.append("&redirect_uri=" + application.getRedirectUri());
            sb.append("&state=").append(state);
            spotifyLoginUrl = new URL(sb.toString());
            Reference redirectTo = new Reference(spotifyLoginUrl);
            getResponse().redirectSeeOther(redirectTo);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }


}
