package io.skysail.server.app.reference.one2manyNoAgg;

import javax.persistence.Id;

import org.hibernate.validator.constraints.NotBlank;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import io.skysail.server.forms.PostView;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Contact implements Identifiable {

    @Id
    private String id;

    @Field
    @PostView(tab = "main")
    private String name;

    @Field
    @NotBlank
    @PostView(tab = "main")
    private String surname;

}
