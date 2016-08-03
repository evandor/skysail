package io.skysail.server.app;

import java.io.IOException;
import java.util.Collections;

import org.raml.emitter.RamlEmitter;
import org.raml.model.Raml;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.ext.apispark.internal.conversion.raml.RamlTranslator;
import org.restlet.ext.apispark.internal.introspection.application.ApplicationIntrospector;
import org.restlet.ext.apispark.internal.model.Definition;
import org.restlet.ext.raml.RamlSpecificationRestlet;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SkysailRamlSpecificationRestlet extends RamlSpecificationRestlet {

    /** The definition of the API. */
    private Definition definition;
    private String basePath;
    private Reference baseRef;

    public SkysailRamlSpecificationRestlet(Context context, SkysailApplication skysailApplication) {
        super(context);
        setApiInboundRoot(skysailApplication);
        setBasePath("http://localhost:2018/demoapp");
        setApiVersion("v33");
    }

    @Override
    public Representation getRaml() {
        Raml raml = RamlTranslator.getRaml(getDefinition());
        raml.setSchemas(Collections.emptyList());
        return new StringRepresentation(new RamlEmitter().dump(raml), MediaType.TEXT_PLAIN);
    }
    @Override
    public void setBasePath(String basePath) {
        this.basePath = basePath;
        // Process basepath and check validity
        this.baseRef = basePath != null ? new Reference(basePath) : null;
        super.setBasePath(basePath);
    }

    private synchronized Definition getDefinition() {
        if (definition == null) {
            synchronized (SkysailRamlSpecificationRestlet.class) {
                definition = ApplicationIntrospector.getDefinition(getApplication(),
                        baseRef, null, false);
                if (definition.getVersion() == null) {
                    definition.setVersion("1.0");
                }
            }
        }

        return definition;
    }

    @Override
    public void handle(Request request, Response response) {
        super.handle(request, response);
        Representation ramlRepresentation = response.getEntity();
        String txt;
        try {
            txt = ramlRepresentation.getText().replace("\"\":", "");
            txt = txt.replace("displayName: \"Finder with no target class: \"\n    get: ", "");
            //txt = txt.replaceAll("[^\\p{InLatin-1Supplement}]", "?");
            /*txt = txt.replace("ú", "");
            txt = txt.replace("ø", "");
            txt = txt.replace("ù", "");
            txt = txt.replace("û", "");
            txt = txt.replace("    char", "    char");*/

            txt = txt.replaceAll("(?s)application\\/x-json-smile.*?application\\/xml", "application/xml");
            System.out.println(txt);



            System.out.println(txt);
            response.setEntity(new StringRepresentation(txt));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

}
