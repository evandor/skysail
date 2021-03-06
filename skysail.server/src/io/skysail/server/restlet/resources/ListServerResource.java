package io.skysail.server.restlet.resources;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.restlet.Restlet;
import org.restlet.data.Method;
import org.restlet.representation.Variant;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import io.skysail.api.doc.ApiDescription;
import io.skysail.api.doc.ApiMetadata;
import io.skysail.api.doc.ApiMetadata.ApiMetadataBuilder;
import io.skysail.api.doc.ApiSummary;
import io.skysail.api.doc.ApiTags;
import io.skysail.api.links.LinkRelation;
import io.skysail.api.metrics.TimerMetric;
import io.skysail.api.responses.ListServerResponse;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.core.model.SkysailEntityModel;
import io.skysail.core.model.SkysailFieldModel;
import io.skysail.core.resources.SkysailServerResource;
import io.skysail.domain.Entity;
import io.skysail.domain.core.ApplicationModel;
import io.skysail.server.ResourceContextId;
import io.skysail.server.domain.jvm.FieldFacet;
import io.skysail.server.domain.jvm.ResourceType;
import io.skysail.server.facets.FacetsProvider;
import io.skysail.server.filter.FilterParser;
import io.skysail.server.restlet.ListRequestHandler;
import io.skysail.server.restlet.filter.AbstractListResourceFilter;
import io.skysail.server.restlet.response.ListResponseWrapper;
import io.skysail.server.utils.params.FilterParamUtils;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * A ListServerResource implementation takes care of a List of Entities.
 *
 * <p>
 * Typically, the request issuer provides headers defining the accepted media
 * types. Depending on those headers this implementation will provide the
 * entities associated with the current URI in various formats such as JSON,
 * Html, etc.
 * </p>
 *
 * <p>
 * In a browser, you can add something like <code>?media=json</code> to the URL
 * to get the desired representation
 * </p>
 *
 * <p>
 * Concrete subclass example:
 * </p>
 *
 * <pre>
 * <code>
 *    private MailApplication app;
 *    ...
 *
 *    {@literal @}Override
 *    protected void doInit() {
 *        app = (MailApplication) getApplication();
 *    }
 *
 *    {@literal @}Override
 *    public List&lt;Company&gt; getEntity() {
 *      Filter filter = new Filter(getRequest());
 *		Pagination pagination = new Pagination(getRequest(), getResponse(), repository.count(filter));
 *		return repository.find(filter, pagination);
 *    }
 *
 *    {@literal @}Override
 *    public List&lt;Link&gt; getLinks() {
 *       return super.getLinks(PostCompanyResource.class);
 *    }
 * </code>
 * </pre>
 *
 * <br>
 * Concurrency note from parent: contrary to the {@link org.restlet.Uniform}
 * class and its main {@link Restlet} subclass where a single instance can
 * handle several calls concurrently, one instance of {@link ServerResource} is
 * created for each call handled and accessed by only one thread at a time.
 *
 */
@Slf4j
@ToString
public abstract class ListServerResource<T extends Entity> extends SkysailServerResource<List<T>> {

    public static final String CONSTRAINT_VIOLATIONS = "constraintViolations";

	private static final String GET_ENTITY_METHOD_NAME = "getEntity";
	private static final String ERASE_ENTITY_METHOD_NAME = "eraseEntity";

    private List<Class<? extends SkysailServerResource<?>>> associatedEntityServerResources;

    private ListRequestHandler<?> requestHandler;

    @Getter
    protected Facets facets = new Facets();

    /**
     * Default constructor without associatedEntityServerResource.
     */
    public ListServerResource() {
        requestHandler = new ListRequestHandler<>(null);
        addToContext(ResourceContextId.LINK_TITLE, "list");
    }

    @Override
    public final ResourceType getResourceType() {
        return ResourceType.LIST;
    }

    /**
     * Constructor which associates this ListServerResource with a corresponding
     * EntityServerResource.
     *
     * @param skysailServerResource
     *            the class
     */
    @SafeVarargs
    public ListServerResource(Class<? extends SkysailServerResource<?>>... skysailServerResource) {
        this();
        if (skysailServerResource != null) {
            this.associatedEntityServerResources = Arrays.asList(skysailServerResource);
        }
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
     * returns the list of entities in the case of a GET request with media
     * types html, json etc.
     *
     * @return the list of entities in html, csv or treeform format
     */
    @Get("html|json|yaml|xml|csv|timeline|carbon|standalone|data")
    // treeform, csv:broken http://stackoverflow.com/questions/24569318/writing-multi-line-csv-with-jacksonrepresentation
    // https://github.com/restlet/restlet-framework-java/issues/928
    public ListServerResponse<T> getEntities(Variant variant) {
    	TimerMetric timerMetric = getMetricsCollector().timerFor(this.getClass(), "getEntities");
        List<T> response = listEntities();
        timerMetric.stop();
        return new ListServerResponse<>(getResponse(), response);
    }

    @Delete("x-www-form-urlencoded:html|html|json")
    public ListServerResponse<T> deleteList(Variant variant) {
        TimerMetric timerMetric = getMetricsCollector().timerFor(this.getClass(), "deleteList");
        if (variant != null) {
            getRequest().getAttributes().put(SKYSAIL_SERVER_RESTLET_VARIANT, variant);
        }
        ListRequestHandler<T> requestHandler = new ListRequestHandler<>(getApplication());
        AbstractListResourceFilter handler = requestHandler.createForList(Method.DELETE);
        Object entity = null;//handler.handle(this, getResponse()).getEntity();
        timerMetric.stop();
        return new ListServerResponse<>(getResponse(), (List<T>) entity);
    }


    @Override
    public LinkRelation getLinkRelation() {
        return LinkRelation.COLLECTION;
    }

    @SuppressWarnings("unchecked")
    private final List<T> listEntities() {
        ListResponseWrapper responseWrapper = requestHandler.createForList(Method.GET).handleList((SkysailServerResource)this, getResponse());
        return (List<T>) responseWrapper.getEntity();
    }

    /**
     * will be called in case of a DELETE request. Override in subclasses if
     * they support DELETE requests.
     *
     * @return the response
     */
    @Override
    @ApiSummary("summary")
    @ApiDescription("desc")
    @ApiTags("tag")
    public SkysailResponse<List<T>> eraseEntity() {
        throw new UnsupportedOperationException();
    }

    public List<Class<? extends SkysailServerResource<?>>> getAssociatedServerResources() {
        return associatedEntityServerResources;
    }

    protected Map<String, FieldFacet> getFacetsFor(Class<?> cls) {
        Map<String, FieldFacet> result = new HashMap<>();
        FacetsProvider facetsProvider = getApplication().getFacetsProvider();
        Optional<SkysailEntityModel> findFirst = getApplicationModel().getEntityValues().stream()
                .filter(v -> v.getId().equals(cls.getName())) // NOSONAR
                .map(SkysailEntityModel.class::cast).findFirst();

        if (findFirst.isPresent()) {
            Collection<SkysailFieldModel> fieldValues = findFirst.get().getFieldValues();
            for (SkysailFieldModel fieldModel : fieldValues) {
                String ident = cls.getName() + "." + fieldModel.getId();
                try {
                    Field declaredField = cls.getDeclaredField(fieldModel.getId());
                    FieldFacet facetFor = facetsProvider.getFacetFor(ident);
                    result.put(fieldModel.getId(), facetFor);
                } catch (Exception e) {

                }
            }
        }
        return result;
    }

    protected void handleFacets(Class<?> cls, List<?> transactions, ApplicationModel applicationModel) {
        FacetsProvider facetsProvider = getApplication().getFacetsProvider();
        Optional<SkysailEntityModel> findFirst = applicationModel.getEntityValues().stream()
                .filter(v -> v.getId().equals(cls.getName())) // NOSONAR
                .map(SkysailEntityModel.class::cast).findFirst();

        if (findFirst.isPresent()) {
            Collection<SkysailFieldModel> fieldValues = findFirst.get().getFieldValues();
            for (SkysailFieldModel fieldModel : fieldValues) {
                String ident = cls.getName() + "." + fieldModel.getF().getName();
                try {
                    Field declaredField = cls.getDeclaredField(fieldModel.getF().getName());
                    FieldFacet facetFor = facetsProvider.getFacetFor(ident);
                    if (facetFor != null) {
                        declaredField.setAccessible(true);
                        FacetBuckets buckets = facetFor.bucketsFrom(declaredField, transactions);

                        FilterParser filterParser = getApplication().getFilterParser();
                        FilterParamUtils filterParamUtils = new FilterParamUtils(declaredField.getName(), getRequest(), filterParser);

                        buckets.setLocation(facetFor, filterParser, filterParamUtils);

                        this.facets.add(facetFor, buckets);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(),e);
                }

            }
        }
    }

}
