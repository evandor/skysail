package io.skysail.server.app.resources;

import io.skysail.api.text.Translation;
import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message implements Identifiable {
	
	@Field
	private String key;
	
	@Field
	private String value;

	public Message(String key, Translation translation) {
		this.id = key;
		this.key = key;
		this.value = translation.getValue();
	}

	private String id;
}
