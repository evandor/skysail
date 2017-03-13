package io.skysail.core.utils;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.core.app.SkysailApplicationService;
import io.skysail.core.resources.SkysailServerResource;
import io.skysail.server.forms.FormField;
import io.skysail.server.model.FieldsFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FormfieldUtils {

    public static Map<String, FormField> determineFormfields(SkysailServerResource<?> resource, SkysailApplicationService appService) {
        return FieldsFactory.getFactory(resource).determineFrom(resource, appService);
    }

    public static Map<String, FormField> determineFormfields(SkysailResponse<?> response, SkysailServerResource<?> resource, SkysailApplicationService appService) {
        return FieldsFactory.getFactory(response).determineFrom(resource, appService);
    }

}
