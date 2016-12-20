package io.skysail.server.model;

import io.skysail.server.forms.FormField;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.utils.*;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import lombok.NonNull;

public class DefaultEntityFieldFactory extends FieldFactory {

    private Class<?> cls;

    public DefaultEntityFieldFactory(@NonNull Class<?> cls) {
        this.cls = cls;
    }

    @Override
    public Map<String,FormField> determineFrom(SkysailServerResource<?> resource) {
        List<String> fields = resource.getFields();
        return ReflectionUtils.getInheritedFields(cls).stream()
                .filter(f -> test(resource, fields, f))
                .map(f -> new FormField(f, resource.getCurrentEntity()))
                .collect(MyCollectors.toLinkedMap(FormField::getId, Function.identity()));
    }

    public Map<String, FormField> determine(Object currentEntity) {
        return ReflectionUtils.getInheritedFields(cls).stream()
                .map(f -> new FormField(f, currentEntity))
                .collect(MyCollectors.toLinkedMap(FormField::getId, Function.identity()));
    }
}
