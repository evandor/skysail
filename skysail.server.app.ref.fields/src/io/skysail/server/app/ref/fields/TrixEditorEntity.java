//package io.skysail.server.app.ref.fields;
//
//import javax.persistence.Id;
//
//import io.skysail.domain.Entity;
//import io.skysail.domain.html.Field;
//import io.skysail.domain.html.InputType;
//import io.skysail.server.codegen.annotations.GenerateResources;
//import lombok.Getter;
//import lombok.Setter;
//import lombok.ToString;
//
//@Getter
//@Setter
//@ToString
//@GenerateResources(application="io.skysail.server.app.ref.fields.FieldsDemoApplication")
//public class TrixEditorEntity implements Entity {
//
//    @Id
//    private String id;
//
//    @Field(inputType = InputType.TRIX_EDITOR)
//    private String editorContent = "initial content";
//
//
//}