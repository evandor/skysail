package io.skysail.server.app.mermaid;

import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.skysail.domain.Entity;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.HtmlPolicy;
import io.skysail.domain.html.InputType;
import io.skysail.server.codegen.annotations.GenerateResources;
import io.skysail.server.polymer.elements.MermaidSvg;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@GenerateResources(application = "io.skysail.server.app.mermaid.MermaidApplication")
public class MermaidDefinition implements Entity {

    @Id
    private String id;

    @Field
    private String title;

    @Field(inputType = InputType.TEXTAREA, htmlPolicy = HtmlPolicy.TRIX_EDITOR)
    private String mermaidDefinition;

    @Field(inputType = InputType.POLYMER, fieldAttributes = {"mermaidDefinition"})
    @JsonIgnore
    private MermaidSvg svg;

}