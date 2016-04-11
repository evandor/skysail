package io.skysail.server.um.verifier.filebased.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.restlet.Request;
import org.restlet.Response;

import io.skysail.server.um.verifier.filebased.FilebasedVerifier;

@RunWith(MockitoJUnitRunner.class)
public class FilebasedVerifierTest {

	@Mock
	private Request request;

	@Mock
	private Response response;

	@Mock
	private ConfigurationAdmin configurationAdmin;

	@InjectMocks
	private FilebasedVerifier verifier;

	private Configuration configuration;

	@Before
	public void setUp() throws IOException {
		configuration = Mockito.mock(Configuration.class);
		Mockito.when(configurationAdmin.getConfiguration(FilebasedVerifier.class.getName())).thenReturn(configuration);
	}

	@Test
	public void creates_default_configuration_if_provided_config_is_empty() throws IOException {
		verifier.activate(new HashMap<>(), null);
		Dictionary<String, Object> props = getDefaultConfig();
		Mockito.verify(configuration).update(props);
	}

	private Dictionary<String, Object> getDefaultConfig() {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		Dictionary<String, Object> props = new Hashtable();
		props.put("users", "admin,user");
		props.put("service.pid",FilebasedVerifier.class.getName());

		props.put("admin.password", "skysail");//"$2a$12$52R8v2QH3vQRz8NcdtOm5.HhE5tFPZ0T/.MpfUa9rBzOugK.btAHS");
		props.put("admin.roles", "admin,developer");
		props.put("admin.id", "#1");

		props.put("user.password", "$2a$12$52R8v2QH3vQRz8NcdtOm5.HhE5tFPZ0T/.MpfUa9rBzOugK.btAHS");
		props.put("user.id", "#2");
		return props;
	}
}
