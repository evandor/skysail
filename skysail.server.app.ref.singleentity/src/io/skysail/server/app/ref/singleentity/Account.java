package io.skysail.server.app.ref.singleentity;

import java.util.Date;

import javax.persistence.Id;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.skysail.domain.Entity;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.InputType;
import io.skysail.server.forms.ListView;
import io.skysail.server.forms.PostView;
import io.skysail.server.forms.PutView;
import io.skysail.server.forms.Visibility;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * the one and only entity in this application.
 *
 * Instances of this entity can be created, updated, retrieved and deleted
 * utilizing the various *Resource-Classes. An accounts name must not be blank
 * and the iban - if provided - has to comply to a regular expression. The creation
 * date is provided by the implementation and the owner is set to the currently
 * logged in user.
 *
 */
@Data
@EqualsAndHashCode(of = {"id"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Account implements Entity {

    /**
     * a skysail server entity needs to implement Entity.
     */
    @Id
    private String id;

    /**
     * a (string-typed) attribute of this entity with a field annotation so that
     * it will be used in the entity rendering. There's a validation rule as well,
     * the field must not be blank.
     */
    @Field
    @NotBlank // not null or empty, trailing whitespaces getting ignored
    private String name;

    @Field
    //http://stackoverflow.com/questions/23471591/regex-for-iban-allowing-for-white-spaces-and-checking-for-exact-length
    @Pattern(regexp = "^$|^DE([0-9a-zA-Z]\\s?){20}$")
    private String iban;

    /**
     * This fields holds the creation date and is set in PostAccountResource when creating the new instance. It
     * should not be provided by the user in the creation form, so the visibility is set to 'hide'.
     */
    @Field(inputType = InputType.DATE)
    @PostView(visibility = Visibility.HIDE)
    @PutView(visibility = Visibility.HIDE)
    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date created;

    @Field(inputType = InputType.READONLY)
    @ListView(hide = true)
    private String owner;

}
