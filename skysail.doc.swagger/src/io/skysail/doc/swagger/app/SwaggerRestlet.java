package io.skysail.doc.swagger.app;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.representation.StringRepresentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.skysail.core.app.SkysailApplication;
import io.skysail.doc.swagger.SwaggerSpec;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ToString(of = {"application"})
public class SwaggerRestlet extends Restlet {

    private SkysailApplication application;

    private ObjectMapper mapper = new ObjectMapper();

    public SwaggerRestlet(SkysailApplication application) {
        this.application = application;
    }

    @Override
    public void handle(Request request, Response response) {
        SwaggerSpec swaggerSpec = new SwaggerSpec(application, request);
        String swaggerApi;
        try {
            swaggerApi = mapper.writeValueAsString(swaggerSpec);
            response.setEntity(new StringRepresentation(swaggerApi));//, MediaType.APPLICATION_YAML));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
    }

}
