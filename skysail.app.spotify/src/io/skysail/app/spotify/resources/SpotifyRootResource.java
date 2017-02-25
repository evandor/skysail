package io.skysail.app.spotify.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.domain.GenericIdentifiable;
import io.skysail.server.restlet.resources.ListServerResource;

public class SpotifyRootResource extends ListServerResource<GenericIdentifiable> {

    @Override
    public List<?> getEntity() {
        return null;
    }
    
    @Override
    public List<Link> getLinks() {
        return super.getLinks(SpotifyLogin.class, SpotifyMeResource.class);
    }

}
