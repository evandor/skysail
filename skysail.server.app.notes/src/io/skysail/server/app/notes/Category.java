package io.skysail.server.app.notes;

import io.skysail.domain.Entity;
import io.skysail.domain.html.Field;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Category implements Entity, Comparable<Category> {

	@Setter
	private String id;

	@Field
	private String name;

	@Override
	public int compareTo(Category o) {
		if (getName() == null) {
			return -1;
		}
		if (o == null || o.getName() == null) {
		    return 1;
		}
		return getName().compareTo(o.getName());
	}

}
