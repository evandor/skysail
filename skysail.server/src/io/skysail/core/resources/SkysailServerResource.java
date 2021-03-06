package io.skysail.core.resources;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.DateTimeConverter;
import org.restlet.Application;
import org.restlet.data.Form;
import org.restlet.data.Reference;
import org.restlet.representation.Variant;
import org.restlet.resource.Options;
import org.restlet.resource.ServerResource;
import org.restlet.security.Role;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.skysail.api.doc.ApiMetadata;
import io.skysail.api.links.Link;
import io.skysail.api.links.LinkRelation;
import io.skysail.api.metrics.MetricsCollector;
import io.skysail.api.metrics.TimerMetric;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.api.text.Translation;
import io.skysail.core.app.SkysailApplication;
import io.skysail.core.app.SkysailApplicationService;
import io.skysail.core.utils.LinkUtils;
import io.skysail.core.utils.ReflectionUtils;
import io.skysail.core.utils.ResourceUtils;
import io.skysail.core.utils.SkysailBeanUtils;
import io.skysail.domain.Entity;
import io.skysail.domain.core.ApplicationModel;
import io.skysail.domain.core.EntityModel;
import io.skysail.server.Constants;
import io.skysail.server.ResourceContextId;
import io.skysail.server.domain.jvm.ResourceType;
import io.skysail.server.forms.FormField;
import io.skysail.server.forms.MessagesUtils;
import io.skysail.server.forms.Tab;
import io.skysail.server.menus.MenuItem;
import io.skysail.server.model.TreeStructure;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.server.restlet.resources.ResourceContextResource;
import io.skysail.server.services.EntityApi;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * Abstract base class for all skysail resources, parameterized with T, the type
 * of the entity handled.
 *
 * <p>
 * The entity can be something concrete (e.g. a contact) or a list of something
 * (e.g. a list of contacts).
 * </p>
 */
@Slf4j
@ToString(exclude = { "links" })
public abstract class SkysailServerResource<T> extends ServerResource {

    public static final String SKYSAIL_SERVER_RESTLET_FORM = "de.twenty11.skysail.server.core.restlet.form";
    public static final String SKYSAIL_SERVER_RESTLET_ENTITY = "de.twenty11.skysail.server.core.restlet.entity";
    public static final String SKYSAIL_SERVER_RESTLET_VARIANT = "de.twenty11.skysail.server.core.restlet.variant";

    public static final String FILTER_PARAM_NAME = "_f";
    public static final String PAGE_PARAM_NAME = "_page";
    public static final String SEARCH_PARAM_NAME = "_search";

    public static final String NO_REDIRECTS = "noRedirects";
    public static final String INSPECT_PARAM_NAME = "_inspect";

    @Getter
    private ResourceType resourceType = ResourceType.GENERIC;

    @Getter
    private Set<String> defaultMediaTypes = new HashSet<>();

    @Setter
    @Getter
    private Object currentEntity;

    private List<Link> links;

    private Map<ResourceContextId, String> stringContextMap = new HashMap<>();

    private SkysailApplication app;

    public SkysailServerResource() {
        DateTimeConverter dateConverter = new DateConverter(null);
        dateConverter.setPattern("yyyy-MM-dd");
        dateConverter.setUseLocaleFormat(true);
        ConvertUtils.deregister(Date.class);
        ConvertUtils.register(dateConverter, Date.class);

        defaultMediaTypes.add("xml");
        defaultMediaTypes.add("json");
        defaultMediaTypes.add("x-yaml");
        defaultMediaTypes.add("csv");
        defaultMediaTypes.add("mailto");

        app = getApplication();
    }

    @Override
    public SkysailApplication getApplication() {
        Application application = super.getApplication();
        return (application instanceof SkysailApplication) ? (SkysailApplication) application : null;
    }

    public MetricsCollector getMetricsCollector() {
        return getApplication().getMetricsCollector();
    }

    public SkysailResponse<T> eraseEntity() {
        return new SkysailResponse<>();
    }

//    @Get
//    public EntityServerResponse<T> getResource(Variant variant) {
//        TimerMetric timerMetric = getMetricsCollector().timerFor(this.getClass(), "getResource");
//        if (variant != null) {
//            getRequest().getAttributes().put(SKYSAIL_SERVER_RESTLET_VARIANT, variant);
//        }
//        T entity = getEntity3();
//        timerMetric.stop();
//        return new EntityServerResponse<>(getResponse(), entity);
//    }
//
//    protected T getEntity3() {
//        RequestHandler<Entity> requestHandler = new RequestHandler<>(getApplication());
//        AbstractResourceFilter<EntityServerResource<Entity>, Entity> chain = requestHandler.createForEntity(org.restlet.data.Method.GET);
//        ResponseWrapper<Entity> wrapper = chain.handle(this, getResponse());
//        return wrapper.getEntity();
//    }


    /**
     * Typically you will query some kind of repository here and return the
     * result (of type T, where T could be a List).
     *
     * @return entity of Type T (can be a list as well)
     */
    public abstract T getEntity();

    public List<String> getPolymerUiExtensions() {
        return Collections.emptyList();
    }

    public T getEntity(String installation) {
        return getEntity();
    }

    public LinkRelation getLinkRelation() {
        return LinkRelation.CANONICAL;
    }

    public ApiMetadata getApiMetadata() {
        return ApiMetadata.builder().build();
    }

    public String getEntityType() {
        Class<?> entityType = (Class<?>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
        if (this instanceof ListServerResource) {
            return "List of " + entityType.getSimpleName();
        }
        return entityType.getSimpleName();
    }

    @Options()
    public final SkysailResponse<ResourceContextResource> doOptions(Variant variant) { // NO_UCD
                                                                                       // (unused
                                                                                       // code)
        TimerMetric timerMetric = getMetricsCollector().timerFor(this.getClass(), "doOptions");
        ResourceContextResource context = new ResourceContextResource(this);
        timerMetric.stop();
        return new SkysailResponse<>(getResponse(), context);
    }

    /**
     * delegates to restlets getAttribute, but will decode the attribute as
     * well.
     */
    @Override
    public String getAttribute(String name) {
        String attribute = super.getAttribute(name);
        if (attribute != null) {
            return Reference.decode(attribute);
        }
        return null;
    }

    public Map<String, Translation> getMessages() {
        Map<String, Translation> msgs = new TreeMap<>();
        String key = getClass().getName() + ".message";
        Translation translated = getApplication().translate(key, "", this);
        msgs.put("content.header", translated);
        return msgs;
    }

    /**
     * get Messages.
     *
     * @param fields
     *            a list of fields
     * @return messages the messages
     */
    public Map<String, Translation> getMessages(Map<String, FormField> fields) {
        Map<String, Translation> msgs = getMessages();
        if (fields == null) {
            return msgs;
        }
        Application application = getApplication();
        fields.values().stream().forEach(f -> {

            Class<? extends Object> entityClass = null;
            if (getCurrentEntity() != null) {
                if (getCurrentEntity() instanceof List && ((List<?>) getCurrentEntity()).size() > 0) {
                    entityClass = ((List<?>) getCurrentEntity()).get(0).getClass();
                } else {
                    entityClass = getCurrentEntity().getClass();
                }
            }

            String baseKey = MessagesUtils.getBaseKey(entityClass, f); // io.skysail.server.app.notes.Note.title
            String fieldName = MessagesUtils.getSimpleName(f); // title
            addTranslation(msgs, application, baseKey, f.getLabel());
            addTranslation(msgs, application, baseKey + ".info", null);
            addTranslation(msgs, application, baseKey + ".placeholder", null);
            addTranslation(msgs, application, baseKey + ".desc", null);
            addTranslation(msgs, application, baseKey + ".polymerPageContent", null);

            String resourceBaseKey = this.getClass().getName() + "." + fieldName; // io.skysail.server.app.notes.resources.PostNoteResource.content
            addTranslation(msgs, application, resourceBaseKey, fieldName);
            addTranslation(msgs, application, resourceBaseKey + ".desc", null);
            addTranslation(msgs, application, resourceBaseKey + ".placeholder", null);

        });

        return msgs;
    }

    protected void addTranslation(Map<String, Translation> msgs, Application application, String key,
            String defaultMsg) {
        Translation translation = ((SkysailApplication) application).translate(key, defaultMsg, this);
        if (translation != null && translation.getValue() != null) {
            msgs.put(key, translation);
        } else if (defaultMsg != null) {
            msgs.put(key, new Translation(defaultMsg, null, Locale.getDefault(), Collections.emptySet()));
        }
    }

    public Class<?> getParameterizedType() {
        return ReflectionUtils.getParameterizedType(getClass());
    }

    /**
     * Reasoning: not overwriting those two (overloaded) methods gives me a
     * jackson deserialization issue. I need to define which method I want to be
     * ignored by jackson.
     *
     * @see org.restlet.resource.ServerResource#setLocationRef(org.restlet.data.Reference)
     */
    @JsonIgnore
    @Override
    public void setLocationRef(Reference locationRef) {
        super.setLocationRef(locationRef);
    }

    /**
     * creates a list of links for the provided {@link SkysailServerResource}
     * classes.
     *
     * <p>
     * This method is executed only once for the current resource, and the
     * result is cached for further requests.
     * </p>
     *
     * <p>
     * If the resource has associated resources, those links are added as well.
     * </p>
     */
    @SafeVarargs
    public final List<Link> getLinks(Class<? extends SkysailServerResource<?>>... classes) {
        if (links == null) {
            links = LinkUtils.fromResources(this, getCurrentEntity(), classes);
        }
        return links;
    }

    @SuppressWarnings("unchecked")
    public List<Link> getLinks(List<Class<? extends SkysailServerResource<?>>> links) {
        return getLinks(links.toArray(new Class[links.size()]));
    }

    /**
     * example: l -&gt; { l.substitute("spaceId", spaceId).substitute("id",
     * getData().getPage().getRid()); };
     *
     * @return consumer for pathSubs
     */
    public Consumer<Link> getPathSubstitutions() {
        return l -> {
            String uri = l.getUri();
            l.setUri(LinkUtils.replaceValues(uri, getRequestAttributes()));
        };
    }

    /**
     * A resource provides a list of links it references. This is the complete
     * list of links, including links the current user is not authorized to
     * follow.
     *
     * @see SkysailServerResource#getAuthorizedLinks()
     *
     */
    public List<Link> getLinks() {
        if (links != null) {
            return links;
        }
        return new ArrayList<>();
    }

    /**
     * Links might be removed from the framework if the current user isn't
     * authorized to call them.
     */
    public List<Link> getAuthorizedLinks() {
        List<Link> allLinks = getLinks();
        if (allLinks == null) {
            return Collections.emptyList();
        }
        return allLinks.stream().filter(this::isAuthorized).collect(Collectors.toList());
    }

    private boolean isAuthorized(@NonNull Link link) {
        boolean authenticated = app.isAuthenticated(getRequest());
        List<Role> clientRoles = getRequest().getClientInfo().getRoles();
        if (!link.getNeedsAuthentication()) {
            return true;
        }
        List<String> clienRoleNames = clientRoles.stream().map(cr -> cr.getName()).collect(Collectors.toList());
        if (link.getRolesPredicate() != null) {
            if (link.getRolesPredicate().apply(clienRoleNames.toArray(new String[clienRoleNames.size()]))) {
                return true;
            }
        } else {
            if (authenticated) {
                return true;
            }
        }
        return false;
    }

    public String redirectTo() {
        return null;
    }

    public String redirectTo(Class<? extends SkysailServerResource<?>> cls) {
        Link linkheader = LinkUtils.fromResource(getApplication(), cls);
        if (linkheader == null) {
            return null;
        }
        getPathSubstitutions().accept(linkheader);
        return linkheader.getUri();
    }

    public void addToContext(ResourceContextId id, String value) {
        stringContextMap.put(id, value);
    }

    public void removeFromContext(ResourceContextId id) {
        stringContextMap.remove(id);
    }

    public String getFromContext(ResourceContextId id) {
        return stringContextMap.get(id);
    }

    protected void setUrlSubsitution(String identifierName, String id, String substitution) {
        Map<String, String> substitutions = new HashMap<>();
        substitutions.put("/" + identifierName + "/" + id, substitution);
        getContext().getAttributes().put(ResourceContextId.PATH_SUBSTITUTION.name(), substitutions);
    }

    protected Entity populate(Entity bean, Form form) {
        System.out.println(form);
        Map<String, Object> valuesMap = new HashMap<>();
        form.getNames().stream().forEach(key -> valuesMap.put(key, null));
        form.copyTo(valuesMap);

        String beanClassName = bean.getClass().getName();

        Map<String, Object> normalizedValuesMap = new HashMap<>();
        Map<String, List<String>> subBeansMap = new HashMap<>();

        valuesMap.keySet().forEach(key -> {
            String classIdentifier = beanClassName + Constants.CLASS_FIELD_NAMES_SEPARATOR;
            if (key.contains(classIdentifier)) {
                normalizedValuesMap.put(key.replace(classIdentifier, ""), valuesMap.get(key));
                form.removeFirst(key);
            } else {
                String[] split = key.split(Constants.CLASS_FIELD_NAMES_SEPARATOR);
                if (split.length == 2) {
                    String subBeanClassName = split[0];
                    String subBeanFieldName = split[1];
                    List<String> beansFields;
                    if (subBeansMap.containsKey(subBeanClassName)) {
                        beansFields = subBeansMap.get(subBeanClassName);
                    } else {
                        beansFields = new ArrayList<>();
                        subBeansMap.put(subBeanClassName, beansFields);
                    }
                    beansFields.add(subBeanFieldName);
                } else {
                    // log.info("what to do with {}?", key);
                }
            }
        });

        Entity populatedBean = populateBean(bean, normalizedValuesMap);

        if (subBeansMap.size() > 0) {
            SkysailApplicationService skysailApplicationService = getApplication()
                    .getSkysailApplicationService();
            if (skysailApplicationService != null) {
                subBeansMap.keySet().stream().forEach(key -> {
                    EntityApi<?> entityApi = skysailApplicationService.getEntityApi(key);
                    Entity subBean = entityApi.create();
                    Entity populatedSubBean = populate(subBean, form);

                    try {
                        Method contactsSetter = populatedBean.getClass().getDeclaredMethod("setContacts", List.class);
                        contactsSetter.invoke(populatedBean, Arrays.asList(populatedSubBean));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }

        return populatedBean;
    }

    protected Entity populate(T entityTemplate, Entity userProvidedEntity) {
        Map<String, Object> valuesMap = new HashMap<>();
        Arrays.stream(entityTemplate.getClass().getDeclaredMethods()).filter(m -> m.getName().startsWith("get"))
                .filter(m -> !m.getName().equals("getId")).forEach(m -> {
                    try {
                        Object invocationResult = m.invoke(userProvidedEntity, new Object[0]);
                        if (invocationResult != null) {
                            String key = m.getName().substring(3);
                            key = key.substring(0, 1).toLowerCase() + key.substring(1);
                            valuesMap.put(key, invocationResult);
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                });
        return populateBean(userProvidedEntity, valuesMap);
    }

    private Entity populateBean(Entity bean, Map<String, Object> valuesMap) {
        try {
            SkysailBeanUtils beanUtilsBean = new SkysailBeanUtils(bean, ResourceUtils.determineLocale(this),
                    getApplication().getSkysailApplicationService());
            beanUtilsBean.populate(bean, valuesMap);
            return bean;
        } catch (Exception e) {
            log.error("Error populating bean {} from form {}", bean, valuesMap, e);
            return null;
        }
    }

    public void copyProperties(T dest, T orig) {
        try {
            SkysailBeanUtils beanUtilsBean = new SkysailBeanUtils(orig, ResourceUtils.determineLocale(this),
                    getApplication().getSkysailApplicationService());
            beanUtilsBean.copyProperties(dest, orig, this);
        } catch (Exception e) {
            throw new RuntimeException("Error copying beans", e);
        }
    }

    protected Map<String, String> describe(T bean) {
        try {
            return BeanUtils.describe(bean);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public List<String> getFields() {
        List<Field> inheritedFields = getInheritedFields(getParameterizedType());
        List<String> result = new ArrayList<>();
        for (Field field : inheritedFields) {
            if (field.getType().isAssignableFrom(List.class)) {
                Type listFieldGenericType = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                List<Field> subfields = getInheritedFields((Class<?>) listFieldGenericType);
                result.addAll(subfields.stream().map(sf -> field.getName() + "." + sf.getName())
                        .collect(Collectors.toList()));
            } else {
                result.add(field.getName());
            }
        }
        return result;

        // return
        // inheritedFields.stream().map(Field::getName).collect(Collectors.toList());
    }

    public List<MenuItem> getAppNavigation() {
        return Collections.emptyList();
    }

    private List<java.lang.reflect.Field> getInheritedFields(Class<?> type) {
        List<java.lang.reflect.Field> result = new ArrayList<>();

        Class<?> i = type;
        while (i != null && i != Object.class) {
            while (i != null && i != Object.class) {
                for (java.lang.reflect.Field field : i.getDeclaredFields()) {
                    if (!field.isSynthetic()) {
                        result.add(field);
                    }
                }
                i = i.getSuperclass();
            }
        }

        return result;
    }

    public Set<String> getRestrictedToMediaTypes() {
        return Collections.emptySet();
    }

    public Set<String> getRestrictedToMediaTypes(String... supportedMediaTypes) {
        HashSet<String> result = new HashSet<>();
        Arrays.stream(supportedMediaTypes).forEach(result::add); // NOSONAR
        return result;
    }

    public <S> S getService(Class<S> cls) {
        return cls.cast(getContext().getAttributes().get(cls.getName()));
    }

    public List<TreeStructure> getTreeRepresentation() {
        return Collections.emptyList();
    }

    public List<Tab> getTabs() {
        return Collections.emptyList();
    }

    public List<Tab> getTabs(Tab... tabs) {
        List<Tab> result = new ArrayList<>();
        Arrays.stream(tabs).forEach(result::add); // NOSONAR
        return result;
    }

    public List<String> getEntityFields() {
        Class<?> type = getParameterizedType();
        ApplicationModel model = getApplication().getApplicationModel();
        Optional<EntityModel<?>> entity = model.getEntityValues().stream().filter(e -> test(e, type)).findFirst();
        if (entity.isPresent()) {
            return new ArrayList<>(entity.get().getFieldNames());
        }
        return Collections.emptyList();
    }

    public ApplicationModel getApplicationModel() {
        return getApplication().getApplicationModel();
    }

    private boolean test(EntityModel e, Class<?> type) {
        return e.getId().equals(type.getName());
    }

    protected void addToRequestAttributesIfAvailable(String identifier, Object value) {
        if (value != null) {
            getRequest().getAttributes().put(identifier, value);
        }
    }

    protected Principal getPrincipal() {
        return getApplication().getAuthenticationService().getPrincipal(getRequest());
    }


}
