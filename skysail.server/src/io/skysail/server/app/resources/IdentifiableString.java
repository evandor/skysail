package io.skysail.server.app.resources;

import java.util.Date;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IdentifiableString implements Identifiable {

	public IdentifiableString(String string) {
		this.payload = string;
	}

	private String id = Long.toString(new Date().getTime());
	
	@Field
	private String payload;

}
