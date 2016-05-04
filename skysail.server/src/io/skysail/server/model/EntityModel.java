package io.skysail.server.model;

import java.util.Map;

import io.skysail.server.forms.FormField;
import io.skysail.server.restlet.resources.SkysailServerResource;
import lombok.ToString;

@ToString
public class EntityModel<R extends SkysailServerResource<?>> {

    private Object entity;
    private R resource;
    
    private Map<String, FormField> fields;

    public EntityModel(Object entity, R resource) {
        this.entity = entity;
        this.resource = resource;
    }


}
