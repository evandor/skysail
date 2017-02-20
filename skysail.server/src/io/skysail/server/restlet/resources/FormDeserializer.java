package io.skysail.server.restlet.resources;

import java.util.LinkedHashMap;
import java.util.Map;

import org.restlet.data.Form;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.skysail.domain.Entity;

public class FormDeserializer<T extends Entity> {

    private static volatile ObjectMapper mapper = new ObjectMapper();

    private Class<?> parameterizedType;

    private String parameterizedTypeName;

    public FormDeserializer(Class<?> parameterizedType) {
        this.parameterizedType = parameterizedType;
        parameterizedTypeName = parameterizedType.getName();
    }

    @SuppressWarnings("unchecked")
    public T createFrom(Form form) {
        if (form == null) {
            return null;
        }
        Map<String, Object> rootMap = new LinkedHashMap<>();
        for (String key : form.getNames()) {
            if ("submit".equals(key)) {
                continue;
            }
            String theKey = key;
            if (key.startsWith(parameterizedTypeName)) {
                theKey = key.substring(parameterizedTypeName.length()+1);
            }
            rootMap.put(theKey, form.getFirstValue(key));
        }
        return (T) FormDeserializer.mapper.convertValue(rootMap, parameterizedType);
    }

}
