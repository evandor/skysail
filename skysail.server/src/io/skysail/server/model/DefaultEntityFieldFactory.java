package io.skysail.server.model;

import java.util.Map;

import io.skysail.server.domain.jvm.SkysailApplicationService;
import io.skysail.server.forms.FormField;
import io.skysail.server.restlet.resources.SkysailServerResource;
import lombok.NonNull;

public class DefaultEntityFieldFactory extends FieldFactory {

    private Class<?> cls;

    public DefaultEntityFieldFactory(@NonNull Class<?> cls) {
        this.cls = cls;
    }

    @Override
    public Map<String,FormField> determineFrom(@NonNull SkysailServerResource<?> resource, @NonNull SkysailApplicationService service) {
        return determine(resource, cls, service);
//        service.getEntityModel(cls.getName());
//        
//        List<String> fields = resource.getFields();
//        return ReflectionUtils.getInheritedFields(cls).stream()
//                .filter(f -> test(resource, fields, f))
//                .map(f -> new FormField(f, resource.getCurrentEntity(), service))
//                .collect(MyCollectors.toLinkedMap(FormField::getId, Function.identity()));
    }

   /* public Map<String, FormField> determine(Object currentEntity) {
        return ReflectionUtils.getInheritedFields(cls).stream()
                .map(f -> new FormField(f, currentEntity, null))
                .collect(MyCollectors.toLinkedMap(FormField::getId, Function.identity()));
    }*/
}
