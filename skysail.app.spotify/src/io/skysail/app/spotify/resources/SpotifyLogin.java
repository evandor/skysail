package io.skysail.app.spotify.resources;

import io.skysail.app.spotify.SpotifyApplication;
import io.skysail.domain.GenericIdentifiable;
import io.skysail.server.restlet.resources.EntityServerResource;

public class SpotifyLogin extends EntityServerResource<GenericIdentifiable> {

    @Override
    public GenericIdentifiable getEntity() {
        SpotifyApplication application = (SpotifyApplication) getApplication();
        application.getSpotifyApi().authorize(getContext(),getResponse());
        return null;
    }

}
