package io.skysail.server.app.resources;

import io.skysail.domain.Identifiable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResourceMetadata implements Identifiable {

	public ResourceMetadata(String id) {
		this.id = id;
	}

	private String id;

}
