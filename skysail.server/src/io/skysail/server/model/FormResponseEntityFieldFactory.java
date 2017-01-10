package io.skysail.server.model;

import io.skysail.server.domain.jvm.SkysailApplicationService;
import io.skysail.server.forms.FormField;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.utils.*;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class FormResponseEntityFieldFactory extends FieldFactory {

    private Class<? extends Object> cls;

    public FormResponseEntityFieldFactory(Class<?> cls) {
        this.cls = cls;
    }

    /**
     * the class object which was provided in the constructor is scanned for its fields and
     * for each of those which are valid in respect to the current resource (see the
     * "test" method) a new FormField is created.
     */
    @Override
    public Map<String,FormField> determineFrom(SkysailServerResource<?> resource, SkysailApplicationService service) {
        List<String> fields = resource.getFields();
        return ReflectionUtils.getInheritedFields(cls).stream()
                .filter(f -> test(resource, fields, f))
                .map(f -> new FormField(f, resource.getCurrentEntity(), service))
                .collect(MyCollectors.toLinkedMap(FormField::getId, Function.identity()));
    }

}
