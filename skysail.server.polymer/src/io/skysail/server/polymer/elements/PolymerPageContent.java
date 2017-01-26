package io.skysail.server.polymer.elements;

import io.skysail.api.text.Translation;
import io.skysail.server.rendering.RenderingMode;
import io.skysail.server.services.PolymerElementDefinition;
import io.skysail.server.utils.CookiesUtils;

public class PolymerPageContent extends PolymerElementDefinition {

    @Override
    public String render() {
        String identifier = getFormFieldAdapter().getName() + ".polymerPageContent";
        Translation translation = getMessages().get(identifier);
        String content = translation != null ? translation.getValue() : "";
        RenderingMode modeFromCookie = CookiesUtils.getModeFromCookie(getRequest());
        String editable = modeFromCookie.equals(RenderingMode.EDIT) ? "editable" : "";
        return "<sky-content identifier='"+identifier+"' content='"+content+"' "+editable+"/>";
    }

}
