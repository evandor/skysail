package io.skysail.server.polymer.elements;

import io.skysail.server.services.PolymerElementDefinition;
import lombok.Setter;

public class PolymerIframeContent extends PolymerElementDefinition {

	@Setter
    private String iframeSrc;

    @Override
    public String render() {
        return "<sky-iframe src=\""+iframeSrc+"\" style=\"width:100%; height:700px; border:none;\" />";
    }

}
