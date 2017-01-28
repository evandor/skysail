package io.skysail.server.app.pline;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import io.skysail.domain.Entity;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.InputType;
import io.skysail.domain.html.Relation;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode(of = "id")
@ToString
@Getter
@Setter
@NoArgsConstructor
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="id")
public class Registration implements Entity {

	@Id
    private String id;

    @Field
    @Size(min = 3)
    private String name;

    @Field//(inputType = InputType.EMAIL)
    @NotNull
    private String email;

    @Field(inputType = InputType.CHECKBOX)
    private boolean confirmed;

    @Field(inputType = InputType.READONLY)
    private String code;

    @Field(inputType = InputType.READONLY)
    private Date validUtil;

    @Relation
    @JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
    private List<Registration> followers = new ArrayList<>();
}