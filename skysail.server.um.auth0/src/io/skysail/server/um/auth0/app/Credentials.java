package io.skysail.server.um.auth0.app;

import io.skysail.domain.Identifiable;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Credentials implements Identifiable {

	private String id;
	private String state;
}
