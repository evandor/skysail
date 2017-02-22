 package io.skysail.server.app.ref.fields.domain;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.skysail.domain.Entity;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.InputType;
import io.skysail.server.app.ref.fields.EmailAddress;
import io.skysail.server.codegen.annotations.GenerateResources;
import io.skysail.server.polymer.elements.PolymerPageContent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@GenerateResources(application="io.skysail.server.app.ref.fields.FieldsDemoApplication")
public class TextEntity implements Entity {

	private static final String CSS_STYLE = "background-color: #fcfcfc;border:1px dotted #9ca2c4;padding:12px 0px 6px 0px;";
    @Id
    private String id;

    @Field(inputType = InputType.POLYMER)
    @JsonIgnore
    private PolymerPageContent content1;

    @Field(cssStyle=CSS_STYLE)
    private String anemptystring;

    @Field(inputType = InputType.POLYMER)
    @JsonIgnore
    private PolymerPageContent content2;

    @Field(cssStyle=CSS_STYLE)
    private String astring = "this string is from the Java AnEntity File " + this.getClass().getName();

    @Field(inputType = InputType.POLYMER)
    @JsonIgnore
    private PolymerPageContent content3;

    @Field(cssStyle=CSS_STYLE)
    private String withPlaceholder;

    @Field(inputType = InputType.POLYMER)
    @JsonIgnore
    private PolymerPageContent content4;

    // https://www.infoq.com/presentations/ddd-rest?utm_medium=social&utm_campaign=postplanner&utm_source=twitter.com
    @Field(cssStyle=CSS_STYLE, inputType = InputType.EMAIL)
    private EmailAddress emailAddress;

    @Field(inputType = InputType.POLYMER)
    @JsonIgnore
    private PolymerPageContent content5;

    @Field(cssStyle=CSS_STYLE)
    @NotNull
    @Size(min=1)
    private String required = "this is required - delete and try to submit!";

}