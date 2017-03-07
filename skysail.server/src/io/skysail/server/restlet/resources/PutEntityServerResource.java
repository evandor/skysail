package io.skysail.server.restlet.resources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.representation.Variant;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;

import io.skysail.api.doc.ApiMetadata;
import io.skysail.api.links.Link;
import io.skysail.api.links.LinkRelation;
import io.skysail.api.metrics.TimerMetric;
import io.skysail.api.responses.FormResponse;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.core.resources.SkysailServerResource;
import io.skysail.domain.Entity;
import io.skysail.server.ResourceContextId;
import io.skysail.server.domain.jvm.ResourceType;
import io.skysail.server.restlet.RequestHandler;
import io.skysail.server.restlet.filter.AbstractResourceFilter;
import io.skysail.server.restlet.response.ResponseWrapper;
import io.skysail.server.utils.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * An abstract resource template dealing with PUT requests (see
 * http://www.ietf.org/rfc/rfc2616.txt, 9.6).
 *
 * <p>
 * Process:
 * </p>
 *
 * <p>
 * Typically, you'd implement doInit to get hold on the id, then getEntity to
 * retrieve the entitiy to be updated, and finally updateEntity to persist it.
 * </p>
 *
 * Example implementing class:
 *
 * <pre>
 *  <code>
 *  public class MyEntityResource extends PutEntityServerResource&lt;MyEntity&gt; {
 *
 *     ...
 *
 *     protected void doInit() throws ResourceException {
 *       super.doInit();
 *       id = getAttribute("id");
 *       app = (MyApplication) getApplication();
 *     }
 *
 *     public T getEntity() {
 *        return app.getRepository().getById(id)
 *     }
 *
 *     public SkysailResponse&lt;?&gt; updateEntity(T entity) {
 *        app.getRepository().update(entity);
 *        return new SkysailResponse&lt;String&gt;();
 *     }
 * }
 * </code>
 * </pre>
 *
 * @param <T>
 */
@Slf4j
public abstract class PutEntityServerResource<T extends Entity> extends SkysailServerResource<T> {

    private String identifierName;
    private String identifier;
    private Class<? extends Entity> parameterizedType;

    public PutEntityServerResource() {
        this("id");
    }

    public PutEntityServerResource(String identifierName) {
        this.identifierName = identifierName;
        addToContext(ResourceContextId.LINK_TITLE, "update");
        addToContext(ResourceContextId.LINK_GLYPH, "edit");
        parameterizedType = (Class<T>) ReflectionUtils.getParameterizedType(this.getClass());
    }

    @Override
    public final ResourceType getResourceType() {
        return ResourceType.PUT;
    }

    @Override
    public ApiMetadata getApiMetadata() {
        return ApiMetadata.builder().build();
    }
    /**
     * If you have a route defined as "/repository/{key}", you can get the key
     * like this: key = (String) getRequest().getAttributes().get("key");
     *
     * <p>
     * To get hold on any parameters passed, consider using this pattern:
     * </p>
     *
     * <p>
     * Form form = new Form(getRequest().getEntity()); action =
     * form.getFirstValue("action");
     * </p>
     *
     */
    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        identifier = getAttribute(identifierName);
    }

    /**
     * will be called in case of a PUT request.
     */
    public abstract void updateEntity(T entity);

    /**
     * The concrete resource should provide a template (a potentially non-valid
     * instance of type T).
     *
     * @return a template instance of type T
     */
    public T createEntityTemplate() {
        try {
            T t = (T) getParameterizedType().newInstance();
            log.warn("have to check this code");
            //t.setId(getAttribute("id"));
            return t;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method will be called by the skysail framework to create the actual
     * resource from its form representation.
     *
     * @param form
     *            the representation of the resource as a form
     * @return the resource of type T
     */
    public Entity getData(Form form) {
        return populate(createEntityTemplate(), form);
    }

    @Get("htmlform|html|json")
    public FormResponse<T> createForm(Variant variant) {
    	TimerMetric timerMetric = getMetricsCollector().timerFor(this.getClass(), "createForm");
        RequestHandler<T> requestHandler = new RequestHandler<>(getApplication());
        AbstractResourceFilter<PutEntityServerResource<T>, T> chain = requestHandler.createForFormResponse();
        ResponseWrapper<T> wrapper = chain.handle(this, getResponse());
        timerMetric.stop();

        return new FormResponse<>(getResponse(), wrapper.getEntity(), identifier, null, redirectBackTo());
    }

    protected String redirectBackTo() {
        return null;
    }

    @Put("json")
    public SkysailResponse<T> putEntity(T entity, Variant variant) {
    	TimerMetric timerMetric = getMetricsCollector().timerFor(this.getClass(), "putEntity");
        getRequest().getAttributes().put(SKYSAIL_SERVER_RESTLET_VARIANT, variant);
        getRequest().getAttributes().put(SKYSAIL_SERVER_RESTLET_ENTITY, entity);
        RequestHandler<T> requestHandler = new RequestHandler<>(getApplication());
        AbstractResourceFilter<PutEntityServerResource<T>, T> handler = requestHandler.createForPut();
        ResponseWrapper<T> handledRequest = handler.handle(this, getResponse());
        timerMetric.stop();

        if (handledRequest.getConstraintViolationsResponse() != null) {
            return handledRequest.getConstraintViolationsResponse();
        }
        return new FormResponse<>(getResponse(), handledRequest.getEntity(),".");
    }

    @Put("x-www-form-urlencoded:html|json")
    public SkysailResponse<T> put(Form form, Variant variant) {
        TimerMetric timerMetric = getMetricsCollector().timerFor(this.getClass(), "put");
        T createFrom = new FormDeserializer<T>(getParameterizedType()).createFrom(form);
        SkysailResponse<T> result = putEntity(createFrom, variant);
        timerMetric.stop();
        return result;



    	// old approach
//        if (form != null) {
//            getRequest().getAttributes().put(SKYSAIL_SERVER_RESTLET_FORM, form);
//        }
//        getRequest().getAttributes().put(SKYSAIL_SERVER_RESTLET_VARIANT, variant);
//        RequestHandler<T> requestHandler = new RequestHandler<>(getApplication());
//        AbstractResourceFilter<PutEntityServerResource<T>, T> handler = requestHandler.createForPut();
//        ResponseWrapper<T> handledRequest = handler.handle(this, getResponse());
//        timerMetric.stop();
//
//        if (handledRequest.getConstraintViolationsResponse() != null) {
//            return handledRequest.getConstraintViolationsResponse();
//        }
//        return new FormResponse<>(getResponse(), handledRequest.getEntity(),".");
    }

    @Override
    public LinkRelation getLinkRelation() {
        return LinkRelation.CREATE_FORM;
    }

    protected Set<ConstraintViolation<T>> validate(T entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Link> getLinks() {

        String ref = getReference().toString();
        if (ref == null) {
            return Collections.emptyList();
        }
        String parentRef = (getReference().getParentRef() != null) ? getReference().getParentRef().toString() : "";

        List<Link> result = new ArrayList<>();
        result.add(new Link.Builder(ref).relation(LinkRelation.NEXT).title("form target").verbs(Method.PUT).build());

        Object entity = this.getCurrentEntity();
        if (entity instanceof Entity) {
            String id = ((Entity)entity).getId().replace("#","");

            result.add(new Link.Builder(parentRef + id).title("delete").verbs(Method.DELETE).build());
        }

        return result;

    }
}
