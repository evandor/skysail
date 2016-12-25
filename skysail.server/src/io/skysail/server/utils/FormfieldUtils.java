package io.skysail.server.utils;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.forms.FormField;
import io.skysail.server.model.FieldsFactory;
import io.skysail.server.restlet.resources.SkysailServerResource;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FormfieldUtils {

    public static Map<String, FormField> determineFormfields(SkysailServerResource<?> resource) {
        return FieldsFactory.getFactory(resource).determineFrom(resource);
    }

    public static Map<String, FormField> determineFormfields(SkysailResponse<?> response, SkysailServerResource<?> resource) {
        return FieldsFactory.getFactory(response).determineFrom(resource);
    }

}
