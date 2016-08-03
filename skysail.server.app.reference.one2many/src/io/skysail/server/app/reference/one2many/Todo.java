package io.skysail.server.app.reference.one2many;

import javax.persistence.Id;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Todo implements Identifiable {

	@Id
	private String id;

	@Field
	private String todoname;
}
