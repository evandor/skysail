 package io.skysail.server.app.ref.fields;

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
@GenerateResources(application="io.skysail.server.app.ref.fields.FieldsDemoApplication")
public class TextEntity implements Entity {

    @Id
    private String id;

    @Field(inputType = InputType.POLYMER)
    private PolymerPageContent content1;

    @Field(cssStyle="background-color: #f3f3f3;border:1px solid gray;padding:12px 0px 6px 0px;")
    private String anemptystring;

    @Field(inputType = InputType.POLYMER)
    private PolymerPageContent content2;

    @Field(cssStyle="background-color: #f3f3f3;border:1px solid gray;padding:12px 0px 6px 0px;")
    private String astring = "this string is from the Java AnEntity File " + this.getClass().getName();

    @Field(inputType = InputType.POLYMER)
    private PolymerPageContent content3;

    @Field(cssStyle="background-color: #f3f3f3;border:1px solid gray;padding:12px 0px 6px 0px;")
    private String withPlaceholder;

    @Field(inputType = InputType.POLYMER)
    private PolymerPageContent content4;

    // https://www.infoq.com/presentations/ddd-rest?utm_medium=social&utm_campaign=postplanner&utm_source=twitter.com
//    @Field(cssStyle="background-color: #f3f3f3;border:1px solid gray;padding:12px 0px 6px 0px;")
//    private EmailAddress emailAddress;

    @Field(inputType = InputType.POLYMER)
    private PolymerPageContent content5;

    @Field(cssStyle="background-color: #f3f3f3;border:1px solid gray;padding:12px 0px 6px 0px;")
    @NotNull
    @Size(min=1)
    private String required = "this is required - delete and try to submit!";

    /*@Value
	static class EmailAddress {
    	String value;
	}*/

}