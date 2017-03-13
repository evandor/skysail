package io.skysail.server.model;

import io.skysail.api.responses.ConstraintViolationsResponse;
import io.skysail.core.app.SkysailApplicationService;
import io.skysail.core.resources.SkysailServerResource;
import io.skysail.core.utils.MyCollectors;
import io.skysail.core.utils.ReflectionUtils;
import io.skysail.server.forms.FormField;
import io.skysail.server.utils.*;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import lombok.NonNull;

public class SkysailResponseEntityFieldFactory extends FieldFactory {

    private Class<? extends Object> cls;
    private ConstraintViolationsResponse<?> source;

    public SkysailResponseEntityFieldFactory(@NonNull ConstraintViolationsResponse<?> source, Class<? extends Object> cls) {
        this.source = source;
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
                .filter(f -> test(resource, fields,f))
                .map(f -> new FormField(f, resource, source))
                .collect(MyCollectors.toLinkedMap(FormField::getId, Function.identity()));
    }

}
