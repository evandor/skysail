package io.skysail.app.spotify.config;

import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Spotify Config")
public @interface SpotifyConfigDescriptor {
    
    String apiBase() default "https://accounts.spotify.com";

    String clientId() default "";

    String clientSecret() default "";

    String redirectUri() default "";

}
