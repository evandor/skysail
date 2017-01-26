package io.skysail.server.polymer.elements;

import io.skysail.api.text.Translation;
import io.skysail.server.services.PolymerElementDefinition;

public class PolymerPageContent extends PolymerElementDefinition {

    @Override
    public String render() {
        String identifier = getFormFieldAdapter().getName() + ".polymerPageContent";
        Translation translation = getMessages().get(identifier);
        String content = translation != null ? translation.getValue() : "";
        return "<sky-content identifier='"+identifier+"' content='"+content+"' editable='true'/>";
    }

}
