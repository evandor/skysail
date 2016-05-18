package io.skysail.server.ext.keyGen;

import static io.skysail.server.product.ProductDefinition.ETC_SERVER_KEY_PRIVATE;
import static io.skysail.server.product.ProductDefinition.ETC_SERVER_KEY_PUBLIC;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;

import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

import lombok.extern.slf4j.Slf4j;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL, configurationPid = "keys")
@Slf4j
public class KeyGenerator {

	@Reference(cardinality = ReferenceCardinality.MANDATORY)
	private volatile ConfigurationAdmin configAdmin;

	@Activate
	public void activate(Map<String, String> config) {
		log.info("Activating {}", this.getClass().getName());
		if (config.get("server.id") != null && exists(ETC_SERVER_KEY_PRIVATE) && exists(ETC_SERVER_KEY_PUBLIC)) {
			return;
		}

		try {
			ProductDefinitionImpl productDefinition = new ProductDefinitionImpl(
					KeyPairGenerator.getInstance("DSA", "SUN"));
			write(productDefinition.installationPublicKey(), ETC_SERVER_KEY_PUBLIC);
			write(productDefinition.installationPrivateKey(), ETC_SERVER_KEY_PRIVATE);
			updateConfiguration(productDefinition);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	@Deactivate
	protected void deactivate(ComponentContext ctxt) {
		log.debug("Deactivating {}", this.getClass().getName());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void updateConfiguration(ProductDefinitionImpl productDefinition)
			throws NoSuchAlgorithmException, IOException {
		String hash = productDefinition.installationPublicKeyHash();
		Dictionary<String, Object> props = configAdmin.getConfiguration("keys").getProperties();
		if (props == null) {
			props = new Hashtable();
		}
		props.put("server.id", hash);
		configAdmin.getConfiguration("keys").update(props);

		String firstConfigDir = System.getProperty("felix.fileinstall.dir").split(",")[0].trim();
		write(("server.id = " + hash + "").getBytes(), firstConfigDir + "/keys.cfg");
	}

	private boolean exists(String filename) {
		return new File(filename).exists();
	}

	private void write(byte[] key, String filename) throws IOException {
		FileOutputStream keyfos = new FileOutputStream(filename);
		keyfos.write(key);
		keyfos.close();
	}

}
