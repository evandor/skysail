package io.skysail.server.ext.keyGen;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

import io.skysail.server.product.ProductDefinition;

public class ProductDefinitionImpl implements ProductDefinition {

	private PrivateKey priv;
	private PublicKey pub;

	public ProductDefinitionImpl(KeyPairGenerator keyPairGenerator) throws NoSuchAlgorithmException, NoSuchProviderException {
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
		keyPairGenerator.initialize(2048, random);
		KeyPair pair = keyPairGenerator.generateKeyPair();
		priv = pair.getPrivate();
		pub = pair.getPublic();
	}

	@Override
	public byte[] installationPublicKey() {
		return pub.getEncoded();
	}

	@Override
	public byte[] installationPrivateKey() {
		return priv.getEncoded();
	}

}
