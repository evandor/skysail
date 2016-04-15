package io.skysail.server.um.verifier.filebased;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;

import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.restlet.security.SecretVerifier;
import org.restlet.security.User;
import org.restlet.security.Verifier;

import io.skysail.server.security.SecurityContext;
import io.skysail.server.security.SecurityContextHolder;
import io.skysail.server.security.token.UsernamePasswordAuthenticationToken;
import lombok.extern.slf4j.Slf4j;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
@Slf4j
public class FilebasedVerifier extends SecretVerifier implements Verifier {

	@Reference
	private volatile ConfigurationAdmin configurationAdmin;

	private UserManagementRepository userManagementRepository;

	@Activate
	public void activate(Map<String, String> config, ComponentContext componentContext) {
		log.info("activating verifier {}", this.getClass().getName());
		if (config.get("users") == null) {
			configureDefault();
			return;
		}
		userManagementRepository = new UserManagementRepository(config);
	}

	public void deactivate() {
		log.info("deactivating verifier {}", this.getClass().getName());
	}

	@Override
	public int verify(String identifier, char[] secret) {
		SecurityContextHolder.clearContext();
		//identifier = identifier.replace("@", "&#64;");
		User user = userManagementRepository.loadUserByUsername(identifier);
		if (user == null) {
			return RESULT_INVALID;
		}
		if (compare(secret, user.getSecret())) {
			SecurityContext securityContext = new SecurityContext(new UsernamePasswordAuthenticationToken(user, user.getSecret()));
			SecurityContextHolder.setContext(securityContext);
			return RESULT_VALID; 
		} else {
			return RESULT_INVALID;
		}
	}

	private void configureDefault() {
		log.warn("creating default configuration for usermanagement as no configuration was provided (yet)!");
		try {
			Configuration config = configurationAdmin.getConfiguration(this.getClass().getName());
			Dictionary<String, Object> props = config.getProperties();
			if (props == null) {
				props = new Hashtable<>();
			}
			props.put("users", "admin,user");
			props.put("service.pid", this.getClass().getName());

			props.put("admin.password", "skysail");//"$2a$12$52R8v2QH3vQRz8NcdtOm5.HhE5tFPZ0T/.MpfUa9rBzOugK.btAHS");
			props.put("admin.roles", "admin,developer");
			props.put("admin.id", "#1");

			props.put("user.password", "$2a$12$52R8v2QH3vQRz8NcdtOm5.HhE5tFPZ0T/.MpfUa9rBzOugK.btAHS");
			props.put("user.id", "#2");

			config.update(props);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

}
