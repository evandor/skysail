package io.skysail.app.spotify.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuthCallbackData {
    
    @JsonProperty("access_token")
    private String accessToken;
    
    private String token_type;
    private String refresh_token;
    private String scope;
    private Integer expires_in;
    

}
