package io.skysail.server.app.reference.one2many;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.Relation;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="id")
@ToString
@Setter
@Getter
public class TodoList implements Identifiable {

	@Id
	private String id;

	@Field
	private String listname;

    @Relation
    @JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
    private List<Todo> todos = new ArrayList<>();

}
