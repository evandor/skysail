package io.skysail.server.app.ref.fields;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import io.skysail.domain.Nameable;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.InputType;
import io.skysail.domain.html.Relation;
import io.skysail.server.codegen.annotations.GenerateResources;
import io.skysail.server.db.validators.UniqueName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@GenerateResources(application = "io.skysail.server.app.ref.fields.FieldsDemoApplication")
@UniqueName(entityClass = NestedEntity.class)
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="id")
public class NestedEntity implements Nameable {

    @Id
    private String id;

    @Field
    private String title;

    @Field(inputType = InputType.TEXTAREA)
    private String content;

    @Relation
    @JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
    List<Comment> comments = new ArrayList<>();

    @Override
    public String getName() {
        return id;
    }

}