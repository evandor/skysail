package io.skysail.server.app.mxgraph.poc;

import javax.persistence.Id;

import io.skysail.domain.Entity;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.InputType;
import io.skysail.server.polymer.elements.PolymerIframeContent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Workflow implements Entity {

	@Id
    private String id = "17";
    
	@Field
    private String iframeSrc = "/mxgraphPOC/v1/examples/editors/workfloweditor.html";
    
    @Field(inputType = InputType.POLYMER, fieldAttributes = {"iframeSrc"})
    private PolymerIframeContent page;


}