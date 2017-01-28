package io.skysail.server.app.pline;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.skysail.domain.Entity;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.InputType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode(of = "id")
@ToString
@Getter
@Setter
public class Confirmation implements Entity {

	@Id
    private String id;

    @Field
    @Size(min = 6,max = 6)
    private String code;

    @Field(inputType = InputType.EMAIL)
    @NotNull
    private String email;

}