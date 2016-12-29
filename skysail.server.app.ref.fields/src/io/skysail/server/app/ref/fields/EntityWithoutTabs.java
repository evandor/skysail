package io.skysail.server.app.ref.fields;

import java.io.Serializable;

import javax.persistence.Id;
import javax.validation.constraints.Size;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.InputType;
import io.skysail.server.codegen.annotations.GenerateResources;
import io.skysail.server.forms.PostView;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@GenerateResources(application="io.skysail.server.app.ref.fields.FieldsDemoApplication")
public class EntityWithoutTabs implements Identifiable, Serializable {

	private static final long serialVersionUID = 5467749853173838976L;

	@Id
	private String id;

	@Field
	@PostView
	private String astring = "@Field private String aString;";

	//@Field
	//@PostView
	//private String iDoNotWorkDueToSecondCapital = "...";

    @Field(inputType = InputType.READONLY)
	@PostView
	private String readonlyString = "I am readonly...";

    @Field
	@PostView
	@Size(min = 2)
	private String requiredString = "I am required...";

}