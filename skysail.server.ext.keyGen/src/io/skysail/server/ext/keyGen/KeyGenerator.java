package io.skysail.server.ext.keyGen;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Activate
	public void activate(Map<String,String> config) {
		log.info("Activating {}", this.getClass().getName());
		if (config.get("server.id") != null) {
			return;
		}
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");

			SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
			keyGen.initialize(1024, random);

			KeyPair pair = keyGen.generateKeyPair();
			PrivateKey priv = pair.getPrivate();
			PublicKey pub = pair.getPublic();

			write(pub.getEncoded(), "etc/serverKey.public");
			write(priv.getEncoded(), "etc/serverKey.private");

			MessageDigest md = MessageDigest.getInstance("SHA-1");

			byte[] hash = md.digest(pub.getEncoded());
			Dictionary<String, Object> props = configAdmin.getConfiguration("keys").getProperties();
			if (props == null) {
				props = new Hashtable();
			}
			props.put("server.id", toHex(hash));
			configAdmin.getConfiguration("keys").update(props);
			
			String firstConfigDir = System.getProperty("felix.fileinstall.dir").split(",")[0].trim();
			write(("server.id = " + toHex(hash) + "").getBytes(), firstConfigDir + "/keys.cfg");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}

	@Deactivate
	protected void deactivate(ComponentContext ctxt) {
		log.debug("Deactivating {}", this.getClass().getName());
	}
	
	private void write(byte[] key, String filename) throws IOException {
		FileOutputStream keyfos = new FileOutputStream(filename);
		keyfos.write(key);
		keyfos.close();
	}
	
	public static String toHex(byte[] bytes) {
	    BigInteger bi = new BigInteger(1, bytes);
	    return String.format("%0" + (bytes.length << 1) + "X", bi);
	}


}
