//package io.skysail.server.app.ref.fields;
//
//import java.io.Serializable;
//import java.net.URL;
//
//import javax.persistence.Id;
//import javax.validation.constraints.Size;
//
//import io.skysail.domain.Identifiable;
//import io.skysail.domain.html.Field;
//import io.skysail.domain.html.InputType;
//import io.skysail.server.app.ref.fields.resources.BookmarkResource;
//import io.skysail.server.forms.ListView;
//import io.skysail.server.forms.PostView;
//import lombok.Getter;
//import lombok.NonNull;
//import lombok.Setter;
//import lombok.ToString;
//
//@Getter
//@Setter
//@ToString
//public class EntityWithTabs implements Identifiable, Serializable {
//
//	private static final long serialVersionUID = 5467749853173838976L;
//
//	@Id
//    private String id;
//
//    @Field
//    @PostView(tab = "tab One")
//    private String astring = "@Field private String aString;";
//
//    @Field
//    @PostView(tab = "tab One")
//    private String iDoNotWorkDueToSecondCapital = "...";
//
//    @Field(inputType = InputType.READONLY)
//    @PostView(tab = "Tab Two")
//    private String readonlyString = "I am readonly...";
//
//    @Field
//    @PostView(tab = "Tab Two")
//    @Size(min=2)
//    private String requiredString = "I am required...";
//
//}