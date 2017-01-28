package io.skysail.server.ext.peers.app;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;

import io.skysail.domain.Entity;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.InputType;
import io.skysail.server.forms.ListView;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PublicPeerDescription implements Entity, Serializable {

	private static final long serialVersionUID = -4793015472674463336L;

	@Id
    private String id;

    @Field
    private String peerIdentifier;

    @Field
    private String ip;

    @Field
    private Integer port;

    @Field(inputType = InputType.DATE)
    private Date pinged;

    @Field
    private PeerStatus status;

    @Field
    @ListView(hide = true)
    private byte[] publicKey;
}
