package io.skysail.server.polymer.elements;

import io.skysail.server.services.PolymerElementDefinition;
import lombok.Setter;

public class MermaidSvg implements PolymerElementDefinition {
    
    @Setter
    private String mermaidDefinition;

    @Override
    public String render() {
        return "<sky-mermaid input='"+mermaidDefinition+"'></sky-mermaid>";
    }

}
