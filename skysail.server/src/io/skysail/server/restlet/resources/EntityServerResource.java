package io.skysail.server.restlet.resources;

import java.text.ParseException;
import java.util.Set;

import javax.validation.ConstraintValidatorFactory;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.representation.Variant;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;

import io.skysail.api.doc.ApiMetadata;
import io.skysail.api.doc.ApiMetadata.ApiMetadataBuilder;
import io.skysail.api.links.LinkRelation;
import io.skysail.api.metrics.TimerMetric;
import io.skysail.api.responses.EntityServerResponse;
import io.skysail.api.responses.FormResponse;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.core.resources.SkysailServerResource;
import io.skysail.domain.Entity;
import io.skysail.server.ResourceContextId;
import io.skysail.server.domain.jvm.ResourceType;
import io.skysail.server.restlet.RequestHandler;
import io.skysail.server.restlet.filter.AbstractResourceFilter;
import io.skysail.server.restlet.response.ResponseWrapper;
import lombok.extern.slf4j.Slf4j;

/**
 * Abstract base class for skysail server-side resources representing a single
 * entity with known id.
 *
 * <p>
 * This class takes care of GET-, PUT- and DELETE-Requests routed to this
 * specific resource.
 * </p>
 *
 * <p>
 * Example usage:
 * </p>
 *
 * <pre>
 *  <code>
 *  public class MyEntityResource extends EntityServerResource&lt;MyEntity&gt; {
 *
 *     private MyApplication app;
 *     private String myEntityId;
 *
 *     protected void doInit() {
 *         myEntityId = getAttribute("id");
 *         app = (MyApplication) getApplication();
 *     }
 *
 *     public MyEntity getEntity() {
 *         return app.getRepository().getById(MyEntity.class, myEntityId);
 *     }
 *
 *     public List&lt;Link&gt; getLinks() {
 *          return super.getLinkheader(PutMyEntityResource.class);
 *     }
 *
 *     public SkysailResponse&lt;?&gt; eraseEntity() {
 *         return null;
 *     }
 *
 * }
 * </code>
 * </pre>
 *
 * @param <T>
 *            the entity
 */
@Slf4j
public abstract class EntityServerResource<T extends Entity> extends SkysailServerResource<T> {

	private static final String GET_ENTITY_METHOD_NAME = "getEntity";
	private static final String ERASE_ENTITY_METHOD_NAME = "eraseEntity";

	public EntityServerResource() {
        addToContext(ResourceContextId.LINK_TITLE, "show");
    }

    @Override
    public final ResourceType getResourceType() {
        return ResourceType.ENTITY;
    }

    /**
     * If you have a route defined as "/somepath/{key}/whatever", you can get
     * the key like this: key = getAttribute("key");
     *
     * <p>
     * To get hold on any parameters passed, consider using this pattern:
     * </p>
     *
     * <p>
     * <code>Form form = new Form(getRequest().getEntity()); action =
     * form.getFirstValue("action");</code>
     * </p>
     *
     */
    @Override
    protected void doInit() { // NOSOAR: For javadoc
        super.doInit();
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
    @Override
    public SkysailResponse<T> eraseEntity() {
        return new SkysailResponse<>();
    }

    /**
     * This method will be called by the skysail framework to create the actual
     * resource from its form representation.
     *
     * @param form
     *            the representation of the resource as a form
     * @return the resource of type T
     * @throws ParseException
     *             parse exception
     */
    public T getData(Form form) throws ParseException {
        log.warn("trying to read data from form {}, but getData(Form form) is not overwritten by {}", form, this
                .getClass().getName());
        return null;

    }

    @Override
    public LinkRelation getLinkRelation() {
        return LinkRelation.ITEM;
    }

    // input: html|json|..., output: html|json|...
    /**
     * @return the response
     */
    @Get("html|json|eventstream|treeform|txt|csv|yaml|mailto|data")
    public EntityServerResponse<T> getResource(Variant variant) {
        TimerMetric timerMetric = getMetricsCollector().timerFor(this.getClass(), "getResource");
        if (variant != null) {
            getRequest().getAttributes().put(SKYSAIL_SERVER_RESTLET_VARIANT, variant);
        }
        T entity = getEntity3();
        timerMetric.stop();
        return new EntityServerResponse<>(getResponse(), entity);
    }

    @Get("htmlform")
    public SkysailResponse<T> getDeleteForm() {
        return new FormResponse<>(getResponse(), getEntity("dummy"), ".", "/");
    }

    @Delete("x-www-form-urlencoded:html|html|json")
    public EntityServerResponse<T> deleteEntity(Variant variant) {
        TimerMetric timerMetric = getMetricsCollector().timerFor(this.getClass(), "deleteEntity");
        if (variant != null) {
            getRequest().getAttributes().put(SKYSAIL_SERVER_RESTLET_VARIANT, variant);
        }
        RequestHandler<T> requestHandler = new RequestHandler<>(getApplication());
        AbstractResourceFilter<EntityServerResource<T>, T> handler = requestHandler.createForEntity(Method.DELETE);
        T entity = handler.handle(this, getResponse()).getEntity();
        timerMetric.stop();
        return new EntityServerResponse<>(getResponse(), entity);
    }

    protected T getEntity3() {
        RequestHandler<T> requestHandler = new RequestHandler<>(getApplication());
        AbstractResourceFilter<EntityServerResource<T>, T> chain = requestHandler.createForEntity(Method.GET);
        ResponseWrapper<T> wrapper = chain.handle(this, getResponse());
        return wrapper.getEntity();
    }

    protected T getEntity4() {
        io.skysail.core.requests.RequestHandler requestHandler = new io.skysail.core.requests.RequestHandler();
        io.skysail.core.requests.AbstractResourceFilter chain = io.skysail.core.requests.RequestHandler.forGet(getApplication());
//        ResponseWrapper<T> wrapper = chain.handle(this, getResponse());
        return null;//wrapper.getEntity();
    }

    protected String getDataAsJson() {
        return "    ";
    }

    public Validator getValidator() {
        return null;
    }

    protected Set<ConstraintViolation<T>> validate(T entity) {
        throw new UnsupportedOperationException();
    }

    protected ConstraintValidatorFactory getConstraintValidatorFactory() {
        return null;
    }

}
