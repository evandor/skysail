package io.skysail.server.ext.ramlconsole.webresource;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import aQute.bnd.annotation.headers.RequireCapability;
import osgi.enroute.namespace.WebResourceNamespace;

@RequireCapability(ns = WebResourceNamespace.NS, filter = "(&(" + WebResourceNamespace.NS
        + "="+RamlConsoleConstants.WEB_RESOURCE_NAME+")${frange;"+RamlConsoleConstants.WEB_RESOURCE_VERSION+"})")
@Retention(RetentionPolicy.CLASS)
public @interface RamlConsoleWebResource {

    /**
     * Define the default resource to return
     * 
     * @return the list of resources to include
     */
    String[] resource() default "grids.css";

    /**
     * Define the priority of this web resources. The higher the priority, the
     * earlier it is loaded when all web resources are combined.
     * 
     * @return the priority
     */
    int priority() default 1000;
}
