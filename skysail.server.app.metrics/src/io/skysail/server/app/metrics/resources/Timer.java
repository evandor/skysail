package io.skysail.server.app.metrics.resources;

import io.skysail.api.metrics.TimerDataProvider;
import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import io.skysail.server.forms.ListView;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Timer implements Identifiable {

    @Field
    private String id;

    @Field
    private long count;

    @Field
    @ListView(format = "%,.2f ms")
    private double min;

    @Field
    @ListView(format = "%,.2f ms")
    private double mean;

    @Field
    @ListView(format = "%,.2f ms")
    private double max;

    public Timer(TimerDataProvider t) {
        id = t.getName();
        count = t.getCount();
        mean = t.getMean();
        min = t.getMin();
        max = t.getMax();
    }

}
