package io.skysail.server.app.events.domain;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.osgi.service.event.Event;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.InputType;
import lombok.Getter;
import lombok.Setter;

@Getter
public class EventDesc implements Identifiable {

    @Setter
    private String id = "id";

    @Field
    private long tstamp;

    @Field
    private String topic;

    @Field(inputType = InputType.TABLE)
    private Map<String, String> data;

    public EventDesc(Event event) {
        topic = event.getTopic();
        tstamp = new Date().getTime();
        this.data = Arrays.stream(event.getPropertyNames())
                .filter(name -> !"event.topics".equals(name))
                .collect(Collectors.toMap(
                        Function.identity(), 
                        name -> event.getProperty(name).toString()));
    }
}
