package io.skysail.server.app.metrics.resources;

import java.util.List;
import java.util.stream.Collectors;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.metrics.MetricsApplication;
import io.skysail.server.restlet.resources.ListServerResource;

public class TimersChartResource extends ListServerResource<Timer> {

    private MetricsApplication app;

    public TimersChartResource() {
        super(BookmarkResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "Chart");
    }

    public TimersChartResource(Class<? extends BookmarkResource> cls) {
        super(cls);
    }

    @Override
    protected void doInit() {
        app = (MetricsApplication) getApplication();
    }

    @Override
    public List<Timer> getEntity() {
        return app.getTimerMetrics().stream().map(t -> new Timer(t)).collect(Collectors.toList());
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(TimersResource.class, TimersChartResource.class);
    }
}