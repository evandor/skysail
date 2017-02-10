package io.skysail.server.product;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public interface ProductDefinition {

	public static final String ETC_SERVER_KEY_PRIVATE = "etc/serverKey.private";
	public static final String ETC_SERVER_KEY_PUBLIC = "etc/serverKey.public";

	byte[] installationPublicKey();

	byte[] installationPrivateKey();

	default String installationPublicKeyHash() throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		byte[] bytes = md.digest(installationPublicKey());
		BigInteger bi = new BigInteger(1, bytes);
		return String.format("%0" + (bytes.length << 1) + "X", bi); // NOSONAR
	}

}
