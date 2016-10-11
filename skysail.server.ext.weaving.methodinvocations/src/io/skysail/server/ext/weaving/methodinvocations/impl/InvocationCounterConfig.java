package io.skysail.server.ext.weaving.methodinvocations.impl;

import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Invocation Counter Config", pid = "invocationCounter")
public @interface InvocationCounterConfig {

    String packagePattern() default "io.skyail";

}
