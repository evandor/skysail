package io.skysail.server.app.reference.singleentity;

import javax.persistence.Id;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Account implements Identifiable {

	@Id
	private String id;
	
	@Field
	private String name;
}
