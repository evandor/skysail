package io.skysail.server.app.ref.one2many;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import io.skysail.domain.Nameable;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.Relation;
import io.skysail.server.db.validators.UniqueName;
import io.skysail.server.forms.PostView;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * This entity is the aggregate root of a one-to-many relation. It is a list of todo items
 * with a name and a comment (in another "tab"). Furthermore, the name of this item is
 * unique, i.e. we will get a validation error if you try to add a new list with a name
 * which already exists.
 *
 */
@EqualsAndHashCode(of = "id")
@ToString
@Setter
@Getter
@UniqueName(entityClass = TodoList.class)
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="id")
public class TodoList implements Nameable {

	@Id
	private String id;

	@Field
	@NotNull
    @NotBlank
    @PostView(tab = "main")
	private String listname;

	@Field
    @PostView(tab = "extra")
    private String comment;

    @Relation
    @JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
    private List<Todo> todos = new ArrayList<>();

    @Override
    public String getName() {
        return listname;
    }

}
