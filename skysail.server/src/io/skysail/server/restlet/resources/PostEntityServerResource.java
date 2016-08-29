package io.skysail.server.restlet.resources;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.representation.Variant;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import com.tinkerpop.blueprints.impls.orient.OrientVertex;

import io.skysail.api.links.Link;
import io.skysail.api.links.LinkRelation;
import io.skysail.api.responses.FormResponse;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.domain.Identifiable;
import io.skysail.server.ResourceContextId;
import io.skysail.server.domain.jvm.ResourceType;
import io.skysail.server.restlet.RequestHandler;
import io.skysail.server.restlet.filter.AbstractResourceFilter;
import io.skysail.server.restlet.filter.CheckBusinessViolationsFilter;
import io.skysail.server.restlet.filter.FormDataExtractingFilter;
import io.skysail.server.restlet.response.ResponseWrapper;
import io.skysail.server.services.PerformanceTimer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * An abstract resource template dealing with POST requests (see
 * http://www.ietf.org/rfc/rfc2616.txt, 9.5), and providing a way
 * to create form-like structures which can be used to actually
 * POST an entity.
 *
 * Process:
 *
 * Restlet framework will call on of the PostEntityServerResource#post methods
 * (depending on the content type of the request), where a responseHandler for a
 * post request is created. This response handler will use a couple of filters
 * to process the request.
 * The {@link FormDataExtractingFilter} will call back to the implementing class
 * (#getData(Form form)) and attach the result to the skysail response data
 * field. Afterwards, the {@link CheckBusinessViolationsFilter} will validate
 * the provided data.
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
public abstract class PostEntityServerResource<T extends Identifiable> extends SkysailServerResource<T> {

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
     * will be called in case of a POST request.
     *
     * Meant to be overwritten if this default behavior is not enough.
     *
     * @param entity
     *            the entity
     */
    public void addEntity(T entity) {
        Class<? extends Identifiable> cls = createEntityTemplate().getClass();
        OrientVertex vertex = (OrientVertex) getApplication().getRepository(cls).save(entity, getApplication().getApplicationModel());
        String id = vertex.getId().toString();
        entity.setId(id);
    }

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
    public T getData(Form form) {
        submitValue = form.getFirstValue("submit");
        T entity = createEntityTemplate();
        this.setCurrentEntity(entity);
        return populate(entity, form);
    }

    public T getData(T entity) {
        T entityTemplate = createEntityTemplate();
        this.setCurrentEntity(entity);
        return populate(entityTemplate, entity);
    }

    // === GET =============================================================================

    @Get("htmlform|html")
    public SkysailResponse<T> createForm() {
        Set<PerformanceTimer> perfTimer = startMonitor(this.getClass(),"createForm");
        log.info("Request entry point: {} @Get('htmlform|html')", this.getClass().getSimpleName());
        List<String> templatePaths = getApplication().getTemplatePaths(this.getClass());
        String formTarget = templatePaths.stream().findFirst().orElse(".");
        List<Link> links = Arrays.asList(new Link.Builder(formTarget).build());
        links.stream().forEach(getPathSubstitutions());

        T entity = createEntityTemplate();
        this.setCurrentEntity(entity);
        stopMonitor(perfTimer);
        return new FormResponse<>(getResponse(), entity, links.get(0).getUri());
    }

    @Get("json")
    public T getJson() {
        Set<PerformanceTimer> perfTimer = startMonitor(this.getClass(),"getJson");
        log.info("Request entry point: {} @Get('json')", this.getClass().getSimpleName());
        RequestHandler<T> requestHandler = new RequestHandler<>(getApplication());
        AbstractResourceFilter<PostEntityServerResource<T>, T> handler = requestHandler.newInstance(Method.GET);
        T entity = handler.handle(this, getResponse()).getEntity();
        stopMonitor(perfTimer);
        return entity;
    }

    // === POST ============================================================================

    @Post("json")
    public SkysailResponse<T> post(T entity, Variant variant) {
        Set<PerformanceTimer> perfTimer = startMonitor(this.getClass(),"post");
        addToRequestAttributesIfAvailable(SKYSAIL_SERVER_RESTLET_ENTITY, entity);
        SkysailResponse<T> post = post((Form) null, variant);
        stopMonitor(perfTimer);
        return post;
    }

    @Post("x-www-form-urlencoded:html")
    public SkysailResponse<T> post(Form form, Variant variant) {
        Set<PerformanceTimer> perfTimer = startMonitor(this.getClass(),"postForm");
        ResponseWrapper<T> handledRequest = doPost(form, variant);
        stopMonitor(perfTimer);
        if (handledRequest.getConstraintViolationsResponse() != null) {
            return handledRequest.getConstraintViolationsResponse();
        }
        return new FormResponse<>(getResponse(), handledRequest.getEntity(), ".");
    }

    private ResponseWrapper<T> doPost(Form form, Variant variant) {
        addToRequestAttributesIfAvailable(SKYSAIL_SERVER_RESTLET_FORM, form);
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
