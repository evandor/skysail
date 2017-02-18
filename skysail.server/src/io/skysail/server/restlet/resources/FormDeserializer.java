package io.skysail.server.restlet.resources;

import java.util.LinkedHashMap;
import java.util.Map;

import org.restlet.data.Form;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.skysail.domain.Entity;

public class FormDeserializer<T extends Entity> {

    private static volatile ObjectMapper mapper = new ObjectMapper();

    private Class<?> parameterizedType;

    public FormDeserializer(Class<?> parameterizedType) {
        this.parameterizedType = parameterizedType;
    }

    public T createFrom(Form form) {
        Map<String, Object> rootMap = new LinkedHashMap<>();
        return (T) this.mapper.convertValue(rootMap, parameterizedType);
    }

}
