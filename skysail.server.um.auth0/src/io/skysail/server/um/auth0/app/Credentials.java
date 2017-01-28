package io.skysail.server.um.auth0.app;

import io.skysail.domain.Entity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Credentials implements Entity {

	private String id;
	private String state;
}
