package io.skysail.server.restlet.resources;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.restlet.data.Method;
import org.restlet.representation.Variant;
import org.restlet.resource.Get;

import io.skysail.api.links.Link;
import io.skysail.api.links.LinkRelation;
import io.skysail.api.metrics.TimerMetric;
import io.skysail.api.responses.EntityServerResponse;
import io.skysail.core.resources.SkysailServerResource;
import io.skysail.domain.Entity;
import io.skysail.server.ResourceContextId;
import io.skysail.server.utils.LinkUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class RedirectResource<T extends Entity> extends SkysailServerResource<T> {

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
    
    @Override
    public Map<Method, Map<String, Object>> getApiMetadata() {
    	return new HashMap<Method, Map<String, Object>>();
    }

    @Get
    public EntityServerResponse<T> redirectToEntity(Variant variant) {
    	TimerMetric timerMetric = getMetricsCollector().timerFor(this.getClass(), "redirectToEntity");
        if (variant != null) {
            getRequest().getAttributes().put(SKYSAIL_SERVER_RESTLET_VARIANT, variant);
        }

        if (redirectToResource() == null) {
        	getResponse().redirectSeeOther(redirectTo());
            timerMetric.stop();
        	return null;
        }

        Link link = LinkUtils.fromResource(getApplication(), redirectToResource());
        getPathSubstitutions().accept(link);
        getResponse().redirectSeeOther(link.getUri());
        timerMetric.stop();
        return new EntityServerResponse<>(getResponse(), null);
    }


}
