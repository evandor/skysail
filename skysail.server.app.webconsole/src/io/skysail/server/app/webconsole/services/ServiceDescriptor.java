package io.skysail.server.app.webconsole.services;

import org.osgi.framework.ServiceReference;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.InputType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceDescriptor implements Identifiable {

	private String id;
	
	@Field(inputType = InputType.TEXT)
	private String symbolicName;

	@Field
	private String version, state;
	
	public ServiceDescriptor(ServiceReference<?> service) {
	}


}
