package io.skysail.app.spotify.resources;

import io.skysail.app.spotify.SpotifyApplication;
import io.skysail.domain.GenericIdentifiable;
import io.skysail.server.restlet.resources.EntityServerResource;

public class SpotifyMePlaylistsResource extends EntityServerResource<GenericIdentifiable> {

    @Override
    public GenericIdentifiable getEntity() {
        SpotifyApplication app = (SpotifyApplication) getApplication();
        String me = app.getSpotifyApi().getPlaylists(getPrincipal(), getResponse());
        return new GenericIdentifiable(me);
    }

}
