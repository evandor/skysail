package io.skysail.server.app.starmoney;

import java.util.Date;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.core.osgi.OsgiDefaultCamelContext;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

@Component
public class CsvReader {

    private OsgiDefaultCamelContext camelContext;

    @Activate
    public void activate(BundleContext bc) throws Exception {
        camelContext = new OsgiDefaultCamelContext(bc);
        System.out.println(camelContext);
        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                Processor myProcessor = new Processor() {
                    @Override
                    public void process(Exchange exchange) {
                        System.out.println("Processing file: " + exchange);
                        Message in = exchange.getIn();
                        in.setBody(new Date().toString() + "\n\n"
                                + in.getBody(String.class));
                    }
                };

                from("file:///tmp/in?noop=true")
                        .process(new SanitizerProcessor())
                        .to("file:///tmp/out")
                        .process(myProcessor)
                        .to("file:///tmp/out2");
            }
        });

        camelContext.start();
    }

    @Deactivate
    public void deactivate() throws Exception {
        camelContext.stop();
        camelContext = null;
    }
}
