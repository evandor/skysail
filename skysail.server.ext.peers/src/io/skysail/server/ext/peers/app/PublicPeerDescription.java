package io.skysail.server.ext.peers.app;

import java.util.Date;

import javax.persistence.Id;

import io.skysail.domain.html.Field;
import io.skysail.domain.html.InputType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PublicPeerDescription implements io.skysail.domain.Identifiable {

    @Id
    private String id;

    @Field
    private String peerIdentifier;

    @Field(inputType = InputType.DATE)
    private Date pinged;

    @Field
    private byte[] publicKey;
}
