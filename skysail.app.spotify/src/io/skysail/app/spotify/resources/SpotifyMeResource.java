package io.skysail.app.spotify.resources;

import io.skysail.app.spotify.SpotifyApplication;
import io.skysail.domain.GenericIdentifiable;
import io.skysail.server.restlet.resources.EntityServerResource;

public class SpotifyMeResource extends EntityServerResource<GenericIdentifiable> {

    private SpotifyApplication app;

    @Override
    protected void doInit()  {
        app = (SpotifyApplication)getApplication();
    }

    @Override
    public GenericIdentifiable getEntity() {
        String me = app.getSpotifyApi().getMe();
        return new GenericIdentifiable(me);
    }


}
