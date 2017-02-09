package io.skysail.server.utils;

import io.skysail.core.app.SkysailApplicationService;
import io.skysail.domain.html.InputType;
import io.skysail.server.forms.FormField;
import io.skysail.server.restlet.resources.SkysailServerResource;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class SkysailBeanUtils {

    private final SkysailBeanUtilsBean beanUtilsBean;
	private final SkysailApplicationService service;

    public SkysailBeanUtils(Object bean, Locale locale, SkysailApplicationService service) {
        this.service = service;
		beanUtilsBean = new SkysailBeanUtilsBean(bean, locale);
    }

    public void populate(Object bean, Map<String, ? extends Object> properties) throws IllegalAccessException, InvocationTargetException {
        beanUtilsBean.populate(bean, properties);
    }

    public void copyProperties(Object dest, Object orig, SkysailServerResource<?> resource) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException  {
        Map<String, FormField> formfields = FormfieldUtils.determineFormfields(resource, service);
        PropertyDescriptor[] origDescriptors = beanUtilsBean.getPropertyUtils().getPropertyDescriptors(orig);
        Class<?> parameterizedType = resource.getParameterizedType();
        for (int i = 0; i < origDescriptors.length; i++) {
            String name = origDescriptors[i].getName();
            String normalizedName = parameterizedType.getName() + "|" + name;
            if ("class".equals(name) || ignore(formfields, normalizedName)) {
                continue;
            }
            if (beanUtilsBean.getPropertyUtils().isReadable(orig, name) &&
                    beanUtilsBean.getPropertyUtils().isWriteable(dest, name)) {
                try {
                    Object value =
                            beanUtilsBean.getPropertyUtils().getSimpleProperty(orig, name);
                    beanUtilsBean.copyProperty(dest, name, value);
                } catch (NoSuchMethodException e) {
                    // Should not happen
                }
            }
        }

    }

    private boolean ignore(Map<String, FormField> formfields, String name) {
        if ("id".equals(name)) {
            return false;
        }
        FormField formField = formfields.get(name);
        if (formField == null) {
            return true;
        }
        if (InputType.READONLY.name().toLowerCase().equals(formField.getInputType())) { // TODO why not use the enum directly?
            return true;
        }
        return false;
    }

}
