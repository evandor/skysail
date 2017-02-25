package io.skysail.app.spotify.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Callback {
    private String access_token,token_type,refresh_token,scope;
    private Integer expires_in;
    

}
