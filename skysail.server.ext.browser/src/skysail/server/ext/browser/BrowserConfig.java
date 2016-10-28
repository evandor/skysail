package skysail.server.ext.browser;

import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Browser Config", pid="browserconfig")
public @interface BrowserConfig {

    String title() default "skysail browser";
    
    int width() default 1340;
    
    int height() default 900;
    
}
