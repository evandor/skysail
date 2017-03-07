package io.skysail.app.instagram.config;

import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "OAuth2 Default Config")
public @interface OAuth2ConfigDescriptor {
    
    String clientId() default "";
    String clientSecret() default "";
    String scope() default "";
    String redirectUri() default "";

}