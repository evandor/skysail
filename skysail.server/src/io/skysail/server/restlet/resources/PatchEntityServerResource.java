package io.skysail.server.restlet.resources;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.representation.Variant;
import org.restlet.resource.Patch;

import io.skysail.api.links.LinkRelation;
import io.skysail.domain.Entity;
import io.skysail.server.domain.jvm.ResourceType;
import io.skysail.server.restlet.RequestHandler;
import io.skysail.server.restlet.filter.AbstractResourceFilter;
import io.skysail.server.restlet.response.ResponseWrapper;
import io.skysail.server.utils.ResourceUtils;
import io.skysail.server.utils.SkysailBeanUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * An abstract resource template dealing with PATCH requests (see
 * http://www.ietf.org/rfc/rfc2616.txt, 9.6).
 *
 * It is assumed that the request is changing only one field of the entity in qustion
 * (indicated be the attribute "fieldname").
 */
@Slf4j
public abstract class PatchEntityServerResource<T extends Entity> extends SkysailServerResource<T> {

    private String newValue;

    @Override
    public final ResourceType getResourceType() {
        return ResourceType.PATCH;
    }

    @Override
    protected Entity populate(Entity bean, Form form) {
        Map<String, Object> valuesMap = new HashMap<>();
        newValue = form.getFirstValue("value");
        valuesMap.put(getAttribute("fieldname"), newValue);
        try {
            SkysailBeanUtils beanUtilsBean = new SkysailBeanUtils(bean, ResourceUtils.determineLocale(this),getApplication().getSkysailApplicationService());
            beanUtilsBean.populate(bean, valuesMap);
            return bean;
        } catch (Exception e) {
            log.error("Error populating bean {} from form {}", bean, valuesMap, e);
            return null;
        }
    }

    @Override
    public Map<Method, Map<String, Object>> getApiMetadata() {
    	return new HashMap<Method, Map<String, Object>>();
    }
    
    /**
     * will be called in case of a PUT request.
     */
    public void updateEntity(T entity) {
        getEntity(null);
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
        return populate(getEntity(null), form);
    }

    protected String redirectBackTo() {
        return null;
    }

    @Patch("x-www-form-urlencoded:html")
    public Object patchEntity(Form form, Variant variant) {
        log.info("Request entry point: {} @Patch({})", this.getClass().getSimpleName(), variant);
        if (form != null) {
            //getRequest().getAttributes().put(SKYSAIL_SERVER_RESTLET_FORM, form);
        }
        if (variant != null) {
            getRequest().getAttributes().put(SKYSAIL_SERVER_RESTLET_VARIANT, variant);
        }
        RequestHandler<T> requestHandler = new RequestHandler<>(getApplication());
        AbstractResourceFilter<PatchEntityServerResource<T>, T> handler = requestHandler.createForPatch();
        ResponseWrapper<T> handledRequest = handler.handle(this, getResponse());
        if (handledRequest.getConstraintViolationsResponse() != null) {
            return handledRequest.getConstraintViolationsResponse();
        }

        return newValue;
    }

    @Override
    public LinkRelation getLinkRelation() {
        return LinkRelation.EDIT;
    }

    protected Set<ConstraintViolation<T>> validate(T entity) {
        throw new UnsupportedOperationException();
    }

}
