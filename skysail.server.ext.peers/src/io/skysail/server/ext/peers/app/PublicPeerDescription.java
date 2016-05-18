package io.skysail.server.ext.peers.app;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublicPeerDescription implements io.skysail.domain.Identifiable {

	@io.skysail.domain.html.Field
	private String id;
	
	@io.skysail.domain.html.Field
	private byte[] publicKey;
}
