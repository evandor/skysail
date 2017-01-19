package io.skysail.server.app.ref.fields;

import javax.persistence.Id;
import javax.validation.constraints.Size;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.InputType;
import io.skysail.server.codegen.annotations.GenerateResources;
import io.skysail.server.forms.ListView;
import io.skysail.server.forms.PostView;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@GenerateResources(application="io.skysail.server.app.ref.fields.FieldsDemoApplication")
public class EntityWithoutTabs implements Identifiable {

    @Id
    private String id;
    
    /**
     * a static input field is just rendered as text and can be used to add addition
     * information inside a form. The actual content is determined by the translation 
     * service.
     */
    @Field(inputType = InputType.STATIC)
    @ListView(hide = true)
    private String explaination = "text before translation...";

    @Field
    @PostView
    private String astring = "this string is from the Java Entity File " + this.getClass().getName();

    // @Field
    // @PostView
    // private String iDoNotWorkDueToSecondCapital = "...";

    @Field(inputType = InputType.READONLY)
    @PostView
    private String readonlyString = "I am readonly...";

    @Field(inputType = InputType.PASSWORD)
    private String aPassword = "secret";

    @Field
    @PostView
    @Size(min = 2)
    private String requiredString = "I am required...";

    @Field(inputType = InputType.TEXTAREA)
    private String aTextarea = "textarea";

}