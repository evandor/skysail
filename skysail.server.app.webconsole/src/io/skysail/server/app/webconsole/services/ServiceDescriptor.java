package io.skysail.server.app.webconsole.services;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.InputType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceDescriptor implements Identifiable {

	@Field
	private String id;
	
	@Field
	private String objectClass;

	@Field
	private String pid;

	@Field
	private String ranking;

	public ServiceDescriptor(ServiceReference<?> ref) {
		id = Long.toString((Long)ref.getProperty(Constants.SERVICE_ID));
		objectClass = Arrays.stream((String[])ref.getProperty(Constants.OBJECTCLASS)).collect(Collectors.joining(", "));
		pid = (String)ref.getProperty(Constants.SERVICE_PID);
		ranking = ref.getProperty(Constants.SERVICE_RANKING) != null ? ref.getProperty(Constants.SERVICE_RANKING).toString() : "";
	}


}
