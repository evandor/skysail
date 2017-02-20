package io.skysail.server.model;

import io.skysail.core.app.SkysailApplicationService;
import io.skysail.core.resources.SkysailServerResource;
import io.skysail.server.forms.FormField;

import java.util.*;

public class NoFieldFactory extends FieldFactory {

    @Override
    public Map<String,FormField> determineFrom(SkysailServerResource<?> resource, SkysailApplicationService service) {
        return Collections.emptyMap();
    }

}
