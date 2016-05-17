package io.skysail.server.ext.peers;

import lombok.Data;

@Data
public class Peer implements io.skysail.domain.Identifiable {

    String id;

    String ip;

    int port;
}
