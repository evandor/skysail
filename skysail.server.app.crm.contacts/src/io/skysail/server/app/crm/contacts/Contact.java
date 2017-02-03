package io.skysail.server.app.crm.contacts;

import java.util.List;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.skysail.domain.Entity;
import io.skysail.domain.html.Field;
import io.skysail.server.app.crm.addresses.Address;
import io.skysail.server.codegen.annotations.GenerateResources;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@GenerateResources(application = "io.skysail.server.app.crm.contacts.ContactsApplication")
public class Contact implements Entity {

    @Id
    private String id;

    @Field
    private String name;

    @Field
    @Size(min=2)
    @NotNull
    private String surname;

    @Field
    private List<Address> addresses;

}