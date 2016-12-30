package io.skysail.server.app.plugins;

import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Server Config", pid="server")
public @interface ObrConfig {

    String urls();

}
