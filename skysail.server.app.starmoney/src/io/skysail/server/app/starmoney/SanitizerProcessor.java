package io.skysail.server.app.starmoney;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SanitizerProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        log.info("Processing file: " + exchange);
        Message in = exchange.getIn();
        String body = in.getBody(String.class);
        System.out.println(body);
        String[] lines = body.split("\\n");

    }

}
