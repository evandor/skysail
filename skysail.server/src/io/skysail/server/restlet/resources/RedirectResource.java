package io.skysail.server.restlet.resources;

import java.util.Set;

import org.restlet.representation.Variant;
import org.restlet.resource.Get;

import io.skysail.api.links.Link;
import io.skysail.api.links.LinkRelation;
import io.skysail.api.responses.EntityServerResponse;
import io.skysail.domain.Identifiable;
import io.skysail.server.ResourceContextId;
import io.skysail.server.utils.LinkUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class RedirectResource<T extends Identifiable> extends SkysailServerResource<T> {

    public RedirectResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Redirection Logic");
    }

    protected abstract Class<? extends SkysailServerResource<?>> redirectToResource();

    @Override
    public T getEntity() {
        return null;
    }

    @Override
    public LinkRelation getLinkRelation() {
        return LinkRelation.ALTERNATE;
    }

    @Get
    public EntityServerResponse<T> redirectToEntity(Variant variant) {
        log.debug("Request entry point: {} @Get()", this.getClass().getSimpleName());
        if (variant != null) {
            getRequest().getAttributes().put(SKYSAIL_SERVER_RESTLET_VARIANT, variant);
        }

        if (redirectToResource() == null) {
        	getResponse().redirectSeeOther(redirectTo());
        	return null;
        }

        Link link = LinkUtils.fromResource(getApplication(), redirectToResource());
        getPathSubstitutions().accept(link);
        getResponse().redirectSeeOther(link.getUri());
        return new EntityServerResponse<>(getResponse(), null);
    }


}
