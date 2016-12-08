package io.skysail.server.app.notes;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class Category implements Identifiable {

	@Setter
	private String id;
	
	@Field
	private String name;

}
