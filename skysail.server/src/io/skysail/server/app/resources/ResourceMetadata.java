package io.skysail.server.app.resources;

import java.util.Map;

import io.skysail.domain.Entity;
import io.skysail.domain.core.EntityModel;
import io.skysail.domain.core.FieldModel;
import io.skysail.domain.html.Field;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResourceMetadata implements Entity {

	@Field
	private String id;
	
	private Map<String, FieldModel> fields;
	
	public ResourceMetadata(String id) {
		this(id,null);
	}

	public ResourceMetadata(String id, EntityModel<?> entity) {
		this.id = id;
		if (entity != null) {
			fields = entity.getFields();
		}
	}


}
