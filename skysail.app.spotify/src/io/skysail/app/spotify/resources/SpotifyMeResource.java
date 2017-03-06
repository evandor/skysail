package io.skysail.app.spotify.resources;

import io.skysail.app.spotify.SpotifyApplication;
import io.skysail.domain.GenericIdentifiable;
import io.skysail.server.restlet.resources.EntityServerResource;

public class SpotifyMeResource extends EntityServerResource<GenericIdentifiable> {

    @Override
    public GenericIdentifiable getEntity() {
        SpotifyApplication app = (SpotifyApplication) getApplication();
        String me = app.getSpotifyApi().getMe(getPrincipal());
        return new GenericIdentifiable(me);
    }

}
