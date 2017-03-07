package io.skysail.app.spotify.config;

import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Spotify Config")
public @interface SpotifyConfigDescriptor {
    
    String clientId() default "";
    String clientSecret() default "";
    String scope() default "";
    String redirectUri() default "";

}