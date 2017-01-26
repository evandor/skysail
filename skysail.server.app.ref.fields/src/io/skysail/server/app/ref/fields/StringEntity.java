package io.skysail.server.app.ref.fields;

import javax.persistence.Id;

import io.skysail.domain.Identifiable;
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
public class StringEntity implements Identifiable {

    @Id
    private String id;

    /**
     * a static input field is just rendered as text and can be used to add addition
     * information inside a form. The actual content is determined by the translation
     * service.
     */
    //@Field(inputType = InputType.STATIC)
    //@ListView(hide = true)
    //private String explaination = "text before translation...";


    @Field(inputType = InputType.POLYMER)
    private PolymerPageContent content1;

    @Field
    private String anemptystring;

    @Field
    private String astring = "this string is from the Java Entity File " + this.getClass().getName();

    @Field
    private String withPlaceholder;

    /*@Field
    @NotNull
    @Size(min=1)
    private String required;*/

}