package io.skysail.server.app.crm.contacts;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import io.skysail.server.codegen.annotations.GenerateResources;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@GenerateResources(application = "io.skysail.server.app.crm.contacts.ContactsApplication")
public class Contact implements Identifiable {

    @Id
    private String id;

    @Field
    private String name;

    @Field
    @Size(min=2)
    @NotNull
    private String surname;

}