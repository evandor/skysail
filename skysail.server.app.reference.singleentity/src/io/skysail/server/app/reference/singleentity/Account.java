package io.skysail.server.app.reference.singleentity;

import javax.persistence.Id;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import lombok.Getter;
import lombok.Setter;

/**
 * the one and only entity in this application.
 *
 * Instances of this entity can be created, updated, retrieved and deleted
 * utilizing the various *Resource-Classes.
 *
 */
@Getter
@Setter
public class Account implements Identifiable {

    /**
     * a skysail server entity needs to implement Identifiable.
     */
    @Id
    private String id;

    /**
     * a (string-typed) attribute of this entity with a field annotation so that
     * it will be used in the entity rendering. There's a validation rule as well,
     * the must must have a size greater 0.
     */
    @Field
    @Size(min = 1)
    private String name;

    @Field
    //http://stackoverflow.com/questions/23471591/regex-for-iban-allowing-for-white-spaces-and-checking-for-exact-length
    @Pattern(regexp = "^DE([0-9a-zA-Z]\\s?){20}$")
    private String iban;

}
