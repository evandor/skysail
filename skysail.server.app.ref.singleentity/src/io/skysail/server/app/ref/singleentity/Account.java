package io.skysail.server.app.ref.singleentity;

import java.util.Date;

import javax.persistence.Id;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.InputType;
import io.skysail.server.forms.PostView;
import io.skysail.server.forms.Visibility;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * the one and only entity in this application.
 *
 * Instances of this entity can be created, updated, retrieved and deleted
 * utilizing the various *Resource-Classes.
 *
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"id"})
public class Account implements Identifiable {

    /**
     * a skysail server entity needs to implement Identifiable.
     */
    @Id
    private String id;

    /**
     * a (string-typed) attribute of this entity with a field annotation so that
     * it will be used in the entity rendering. There's a validation rule as well,
     * the field must not be blank.
     */
    @Field
    @NotBlank
    private String name;

    @Field
    //http://stackoverflow.com/questions/23471591/regex-for-iban-allowing-for-white-spaces-and-checking-for-exact-length
    @Pattern(regexp = "^DE([0-9a-zA-Z]\\s?){20}$")
    private String iban;

    /**
     * This fields holds the creation date and is set in PostAccountResource when creating the new instance. It
     * should not be provided by the user in the creation form, so the visibility is set to 'hide'.
     */
    @Field(inputType = InputType.DATE)
    @PostView(visibility = Visibility.HIDE)
    private Date created;

}
