package io.skysail.server.app.ref.fields.domain;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.skysail.domain.Entity;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.InputType;
import io.skysail.server.codegen.annotations.GenerateResources;
import io.skysail.server.polymer.elements.PolymerPageContent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
//@GenerateResources(application="io.skysail.server.app.ref.fields.FieldsDemoApplication")
public class PasswordEntity implements Entity {

    @Id
    private String id;

    @Field(inputType = InputType.POLYMER)
    private PolymerPageContent content1;

    @Field(inputType = InputType.PASSWORD, cssStyle="background-color: #f3f3f3;border:1px solid gray;padding:12px 0px 6px 0px;")
    private String apassword;

    @Field(inputType = InputType.POLYMER)
    private PolymerPageContent content2;

    @Field(inputType = InputType.PASSWORD, cssStyle="background-color: #f3f3f3;border:1px solid gray;padding:12px 0px 6px 0px;")
    private String astring = "this string is from the Java AnEntity File " + this.getClass().getName();

    @Field(inputType = InputType.POLYMER)
    private PolymerPageContent content3;

    @Field(inputType = InputType.PASSWORD, cssStyle="background-color: #f3f3f3;border:1px solid gray;padding:12px 0px 6px 0px;")
    private String withPlaceholder;

    @Field
    @NotNull
    @Size(min=1)
    private String required;

}