package io.skysail.server.restlet.resources;

import java.util.List;
import java.util.Set;

import org.restlet.data.ClientInfo;
import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.representation.Variant;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import io.skysail.api.links.LinkRelation;
import io.skysail.api.responses.ListServerResponse;
import io.skysail.api.responses.RelationTargetResponse;
import io.skysail.domain.Identifiable;
import io.skysail.server.ResourceContextId;
import io.skysail.server.domain.jvm.ResourceType;
import io.skysail.server.restlet.RelationTargetListRequestHandler;
import io.skysail.server.restlet.filter.AbstractResourceFilter;
import io.skysail.server.restlet.response.ListResponseWrapper;
import io.skysail.server.services.PerformanceTimer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class PostRelationResource<FROM extends Identifiable, TO extends Identifiable> // NOSONAR
        extends SkysailServerResource<List<TO>> {

    private RelationTargetListRequestHandler<FROM, TO> requestHandler;

    /**
     * Default constructor without associatedEntityServerResource.
     */
    public PostRelationResource() {
        requestHandler = new RelationTargetListRequestHandler<>(null);
        addToContext(ResourceContextId.LINK_TITLE, "relations");
    }

    @Override
    public final ResourceType getResourceType() {
        return ResourceType.POST_RELATION;
    }


    protected abstract List<TO> getRelationTargets(String selectedValues);

    public abstract void addRelations(List<TO> entity);

    @Get("html|json|yaml|xml|csv|timeline|standalone|data")
    public RelationTargetResponse<TO> getEntities(Variant variant) {
        Set<PerformanceTimer> perfTimer = startMonitor(this.getClass(),"getEntities");
        log.info("Request entry point: {} @Get({})", this.getClass().getSimpleName(), variant);
        List<TO> response = listTargetEntities();
        stopMonitor(perfTimer);
        return new RelationTargetResponse<>(getResponse(), response);
    }

    @Post("x-www-form-urlencoded:html")
    public ListServerResponse<TO> post(Form form, Variant variant) {
        Set<PerformanceTimer> perfTimer = startMonitor(this.getClass(),"postForm");
        ListResponseWrapper<TO> handledRequest = doPost(form, variant);
        stopMonitor(perfTimer);
        return new ListServerResponse<>(getResponse(), handledRequest.getEntity());
    }

    @SuppressWarnings("unchecked")
    private final List<TO> listTargetEntities() {
        ListResponseWrapper<?> responseWrapper = requestHandler.createForRelationTargetList(Method.GET).handleList(this,
                getResponse());
        return (List<TO>) responseWrapper.getEntity();
    }

    public List<TO> getData(Form form) {
        String selectedValues = form.getValues("selected");
        return getRelationTargets(selectedValues);
    }


    private ListResponseWrapper<TO> doPost(Form form, Variant variant) {
        log.info("Request entry point: {} @Post('x-www-form-urlencoded:html|json|xml')", this.getClass()
                .getSimpleName());
        ClientInfo ci = getRequest().getClientInfo();
        log.info("calling post(Form), media types '{}'", ci != null ? ci.getAcceptedMediaTypes() : "test");
        if (form != null) {
            getRequest().getAttributes().put(SKYSAIL_SERVER_RESTLET_FORM, form);
        }
        if (variant != null) {
            getRequest().getAttributes().put(SKYSAIL_SERVER_RESTLET_VARIANT, variant);
        }
        AbstractResourceFilter<PostRelationResource<FROM,TO>, TO> handler = requestHandler.createForPost();
        getResponse().setStatus(Status.SUCCESS_CREATED);
        return handler.handleList(this, getResponse());
    }


    @Override
    public LinkRelation getLinkRelation() {
        return LinkRelation.RELATED;
    }



}
