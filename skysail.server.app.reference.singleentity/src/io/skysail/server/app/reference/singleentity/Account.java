package io.skysail.server.app.reference.singleentity;

import javax.persistence.Id;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import lombok.Getter;
import lombok.Setter;

/**
 * the one and only entity in this application. 
 * 
 * Instances of this entity can be created, updated, retrieved and deleted utilizing
 * the various *Resource-Classes.
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
	 * a (string-typed) attribute of this entity with a field annotation so that it will
	 * be used in the entity rendering. 
	 */
	@Field
	private String name;
}
