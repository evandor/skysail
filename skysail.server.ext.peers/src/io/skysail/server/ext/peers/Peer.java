package io.skysail.server.ext.peers;

import io.skysail.domain.Entity;
import lombok.Data;
import lombok.NonNull;

@Data
public class Peer implements Entity {

    private String id;

    private final String ip;

    private final Integer port;

    public Peer(@NonNull String id, @NonNull String ip, Integer port) {
		this.id = id;
		this.ip = ip;
		this.port = port;
	}

	public String getServer() {
		return "http://" + (port == null ? ip : ip + ":" + port);
	}
}
