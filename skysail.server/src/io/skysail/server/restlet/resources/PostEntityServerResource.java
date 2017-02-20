package io.skysail.server.restlet.resources;

import java.util.Arrays;
import java.util.List;

import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.representation.Variant;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import io.skysail.api.links.Link;
import io.skysail.api.links.LinkRelation;
import io.skysail.api.metrics.TimerMetric;
import io.skysail.api.responses.FormResponse;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.domain.Entity;
import io.skysail.server.ResourceContextId;
import io.skysail.server.domain.jvm.ResourceType;
import io.skysail.server.restlet.RequestHandler;
import io.skysail.server.restlet.filter.AbstractResourceFilter;
import io.skysail.server.restlet.filter.CheckBusinessViolationsFilter;
import io.skysail.server.restlet.filter.FormDataExtractingFilter;
import io.skysail.server.restlet.response.ResponseWrapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * An abstract resource template dealing with POST requests (see
 * http://www.ietf.org/rfc/rfc2616.txt, 9.5), and providing a way to create
 * form-like structures which can be used to actually POST an entity.
 *
 * Process:
 *
 * Restlet framework will call on of the PostEntityServerResource#post methods
 * (depending on the content type of the request), where a responseHandler for a
 * post request is created. This response handler will use a couple of filters
 * to process the request. The {@link FormDataExtractingFilter} will call back
 * to the implementing class (#getData(Form form)) and attach the result to the
 * skysail response data field. Afterwards, the
 * {@link CheckBusinessViolationsFilter} will validate the provided data.
 *
 * Example implementing class:
 *
 * <pre>
 *  <code>
 *  public class MyEntityResource extends PostEntityServerResource&lt;MyEntity&gt; {
 *
 *     private MyApplication app;
 *     private String myEntityId;
 *
 *     public void doInit() {
 *        app = (MyApplication) getApplication();
 *     }
 *
 *     public MyEntity createEntityTemplate() {
 *         return new MyEntity();
 *     }
 *
 *     public MyEntity getData(Form form) {
 *         return populate(createEntityTemplate(), form);
 *     }
 *
 *    public SkysailResponse&lt;?&gt; addEntity(Clip entity) {
 *        String id = app.getRepository(Account.class).save(entity, app.getApplicationModel()).toString();
 *        entity.setId(id);
 *    }
 *
 * }
 * </code>
 * </pre>
 *
 * @param <T>
 */
@Slf4j
public abstract class PostEntityServerResource<T extends Entity> extends SkysailServerResource<T> {

    @Getter
    private LinkRelation linkRelation = LinkRelation.CREATE_FORM;

    @Getter
    private final ResourceType resourceType = ResourceType.GENERIC; // NOSONAR

    /** the value of the submit button */
    protected String submitValue;

    public PostEntityServerResource() {
        addToContext(ResourceContextId.LINK_TITLE, "create");
        addToContext(ResourceContextId.LINK_GLYPH, "plus");
    }

    /**
     * The concrete resource should provide a template (a potentially non-valid
     * instance of type T).
     *
     * @return a template instance of type T
     */
    public abstract T createEntityTemplate();

    /**
     * Overwrite to make sure the entity is created correctly, e.g. if you want
     * to set a creation date.
     */
    protected void afterPost(Entity entity) {
    }

    /**
     * will be called in case of a POST request.
     *
     *
     * @param entity
     *            the entity
     */
    public abstract void addEntity(T entity);

    @Override
    public T getEntity() {
        return createEntityTemplate();
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
        submitValue = form.getFirstValue("submit");
        T entity = createEntityTemplate();
        this.setCurrentEntity(entity);
        Entity result = populate(entity, form);
        afterPost(result);
        return result;
    }

    public Entity getData(T entity) {
        T entityTemplate = createEntityTemplate();
        this.setCurrentEntity(entity);
        Entity result = populate(entityTemplate, entity);
        afterPost(result);
        return result;
    }

    // === GET =============================================================================

    @Get("htmlform|html")
    public SkysailResponse<T> createForm() {
        TimerMetric timerMetric = getMetricsCollector().timerFor(this.getClass(), "createForm");
        log.debug("Request entry point: {} @Get('htmlform|html')", this.getClass().getSimpleName());
        List<String> templatePaths = getApplication().getTemplatePaths(this.getClass());
        String formTarget = templatePaths.stream().findFirst().orElse(".");
        List<Link> links = Arrays.asList(new Link.Builder(formTarget).build());
        links.stream().forEach(getPathSubstitutions());

        T entity = createEntityTemplate();
        this.setCurrentEntity(entity);
        timerMetric.stop();
        return new FormResponse<>(getResponse(), entity, links.get(0).getUri());
    }

    @Get("json")
    public T getJson() {
        TimerMetric timerMetric = getMetricsCollector().timerFor(this.getClass(), "getJson");
        log.debug("Request entry point: {} @Get('json')", this.getClass().getSimpleName());
        RequestHandler<T> requestHandler = new RequestHandler<>(getApplication());
        AbstractResourceFilter<PostEntityServerResource<T>, T> handler = requestHandler.newInstance(Method.GET);
        T entity = handler.handle(this, getResponse()).getEntity();
        timerMetric.stop();
        return entity;
    }

    // === POST ============================================================================

    @Post("json")
    public SkysailResponse<T> post(T entity, Variant variant) {
        TimerMetric timerMetric = getMetricsCollector().timerFor(this.getClass(), "postjson");
        addToRequestAttributesIfAvailable(SKYSAIL_SERVER_RESTLET_ENTITY, entity);
        ResponseWrapper<T> handledRequest = doPost(entity, variant);
        timerMetric.stop();

        if (handledRequest.getConstraintViolationsResponse() != null) {
             return handledRequest.getConstraintViolationsResponse();
        }
        return new FormResponse<>(getResponse(), handledRequest.getEntity(), ".");

    }

    @Post("x-www-form-urlencoded:html")
    public SkysailResponse<T> post(Form form, Variant variant) {
        TimerMetric timerMetric = getMetricsCollector().timerFor(this.getClass(), "posthtml");
//        if (form == null) {
//            return new SkysailResponse();
//        }
        T entity = new FormDeserializer<T>(getParameterizedType()).createFrom(form);
        SkysailResponse<T> result = post(entity, variant);
        timerMetric.stop();
        return result;
        //
        // ResponseWrapper<T> handledRequest = doPost(form, variant);
        // timerMetric.stop();
        // if (handledRequest.getConstraintViolationsResponse() != null) {
        // return handledRequest.getConstraintViolationsResponse();
        // }
        // return new FormResponse<>(getResponse(), handledRequest.getEntity(),
        // ".");
    }

    private ResponseWrapper<T> doPost(T entity, Variant variant) {
        addToRequestAttributesIfAvailable(SKYSAIL_SERVER_RESTLET_ENTITY, entity);
        addToRequestAttributesIfAvailable(SKYSAIL_SERVER_RESTLET_VARIANT, variant);
        RequestHandler<T> requestHandler = new RequestHandler<>(getApplication());
        AbstractResourceFilter<PostEntityServerResource<T>, T> handler = requestHandler.createForPost();
        getResponse().setStatus(Status.SUCCESS_CREATED);
        return handler.handle(this, getResponse());
    }

    @Override
    public List<Link> getLinks() {
        return Arrays.asList(new Link.Builder(".").relation(LinkRelation.NEXT).title("form target").verbs(Method.POST)
                .build());
    }
}
