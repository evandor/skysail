package io.skysail.app.spotify.resources;

import io.skysail.app.spotify.SpotifyApplication;
import io.skysail.domain.GenericIdentifiable;
import io.skysail.server.restlet.resources.EntityServerResource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SpotifyLoginCallback2 extends EntityServerResource<GenericIdentifiable> {

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
        String storedState = (String) getContext().getAttributes().get(SpotifyApplication.SPOTIFY_AUTH_STATE);
        if (state == null || !state.equals(storedState)) {
            log.warn("state does not match when logging in to spotify, redirecting to root");
//            Reference redirectTo = new Reference("/");
//            getResponse().redirectSeeOther(redirectTo);
//            return null;
        }

        getContext().getAttributes().remove(SpotifyApplication.SPOTIFY_AUTH_STATE);
        //String callbackJson = application.getSpotifyApi().getToken(code);
        //ApiServices.setAccessData(getPrincipal(), callbackJson);
        String target = (String) getContext().getAttributes().get("oauthTarget");
        target += "?code=" + code +"&state=" + state;
        log.info("redirecting to '{}'", target);
        getResponse().redirectTemporary(target);
        return null;
    }

}
