package io.skysail.server.ext.cloner;

import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Cloner Config", pid="cloner")
public @interface ClonerConfig {

    String clonerBaseDir() default "";

}
