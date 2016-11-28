package io.skysail.server.app.starmoney.camel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        in.setBody(in.getBody(String.class));
        String body = in.getBody(String.class);
        String[] input = body.split("\\n");
        List<String> output = new ArrayList<>();
        Arrays.stream(input).forEach(l -> {
            output.add(l.replaceAll("\\;\\;", ";<NULL>;"));
        });
        exchange.getIn().setBody(output.stream().collect(Collectors.joining("\n")));
    }

}
