package io.skysail.server.app.ref.fields;

import io.skysail.domain.Identifiable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode
public class EmailAddress implements Identifiable {

	@Setter
	private String id;


	private String value;
}
