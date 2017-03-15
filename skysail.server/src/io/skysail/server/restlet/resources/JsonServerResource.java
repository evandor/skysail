package io.skysail.server.restlet.resources;

import java.util.Set;

import javax.validation.ConstraintValidatorFactory;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.restlet.data.Method;
import org.restlet.representation.Variant;
import org.restlet.resource.Get;

import io.skysail.api.doc.ApiMetadata;
import io.skysail.api.doc.ApiMetadata.ApiMetadataBuilder;
import io.skysail.api.links.LinkRelation;
import io.skysail.api.metrics.TimerMetric;
import io.skysail.api.responses.EntityServerResponse;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.core.resources.SkysailServerResource;
import io.skysail.domain.GenericIdentifiable;
import io.skysail.server.ResourceContextId;
import io.skysail.server.domain.jvm.ResourceType;
import io.skysail.server.restlet.RequestHandler;
import io.skysail.server.restlet.filter.AbstractResourceFilter;
import io.skysail.server.restlet.response.ResponseWrapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class JsonServerResource<T extends GenericIdentifiable> extends SkysailServerResource<T> {

	private static final String GET_ENTITY_METHOD_NAME = "getEntity";
	private static final String ERASE_ENTITY_METHOD_NAME = "eraseEntity";

	public JsonServerResource() {
        addToContext(ResourceContextId.LINK_TITLE, "jsonrequest");
    }

    @Override
    public final ResourceType getResourceType() {
        return ResourceType.ENTITY;
    }

    public String getId() {
        return null;
    }

    @Override
    public ApiMetadata getApiMetadata() {
        ApiMetadataBuilder apiMetadata = ApiMetadata.builder();

        apiMetadata.summaryForGet(this.getClass(),GET_ENTITY_METHOD_NAME);
        apiMetadata.descriptionForGet(this.getClass(),GET_ENTITY_METHOD_NAME);
        apiMetadata.tagsForGet(this.getClass(),GET_ENTITY_METHOD_NAME);

        apiMetadata.summaryForDelete(this.getClass(),ERASE_ENTITY_METHOD_NAME);
        apiMetadata.descriptionForGet(this.getClass(),GET_ENTITY_METHOD_NAME);
        apiMetadata.tagsForGet(this.getClass(),GET_ENTITY_METHOD_NAME);

        return apiMetadata.build();
    }


    /**
     * will be called in case of a DELETE request. Override in subclasses if
     * they support DELETE requests.
     *
     * @return the response
     */
    public SkysailResponse<?> eraseEntity() {
        return new SkysailResponse<>();
    }

    @Override
    public LinkRelation getLinkRelation() {
        return LinkRelation.ITEM;
    }

    /**
     * @return the response
     */
    @Get("json")
    public EntityServerResponse<GenericIdentifiable> getResource(Variant variant) {
        TimerMetric timerMetric = getMetricsCollector().timerFor(this.getClass(), "getResource");
        if (variant != null) {
            getRequest().getAttributes().put(SKYSAIL_SERVER_RESTLET_VARIANT, variant);
        }
        GenericIdentifiable entity = getEntity3();
        timerMetric.stop();
        return new EntityServerResponse<>(getResponse(), entity);
    }

    protected GenericIdentifiable getEntity3() {
        RequestHandler<GenericIdentifiable> requestHandler = new RequestHandler<>(getApplication());
        AbstractResourceFilter<JsonServerResource, GenericIdentifiable> chain = requestHandler.createForJson(Method.GET);
        ResponseWrapper<GenericIdentifiable> wrapper = chain.handle(this, getResponse());
        return wrapper.getEntity();
    }

    public Validator getValidator() {
        return null;
    }

    protected Set<ConstraintViolation<GenericIdentifiable>> validate(GenericIdentifiable entity) {
        throw new UnsupportedOperationException();
    }

    protected ConstraintValidatorFactory getConstraintValidatorFactory() {
        return null;
    }

}
