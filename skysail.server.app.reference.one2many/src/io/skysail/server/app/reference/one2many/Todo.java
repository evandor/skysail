package io.skysail.server.app.reference.one2many;

import javax.persistence.Id;

import org.hibernate.validator.constraints.NotBlank;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import io.skysail.server.forms.PostView;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Todo implements Identifiable {

    @Id
    private String id;

    @Field
    @NotBlank
    @PostView(tab = "main")
    private String todoname;

    @PostView(tab = "extra")
    private String comment;
}
