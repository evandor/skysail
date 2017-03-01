package io.skysail.ext.oauth2.config;

import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "OAuth2 Default Config")
public @interface OAuth2ConfigDescriptor {
    
    String clientId() default "";
    String clientSecret() default "";
    String redirectUri() default "";

}
