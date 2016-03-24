package io.skysail.server.app.demo;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.event.EventAdmin;

import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import io.skysail.domain.Identifiable;
import io.skysail.domain.core.Repositories;
import io.skysail.server.app.ApiVersion;
import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.demo.resources.PostTimetableResourceGen;
import io.skysail.server.app.demo.resources.PutTimetableResourceGen;
import io.skysail.server.app.demo.resources.TimetableResourceGen;
import io.skysail.server.app.demo.resources.TimetablesResourceGen;
import io.skysail.server.menus.MenuItemProvider;

@Component(immediate = true)
public class DemoApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String LIST_ID = "lid";
    public static final String TODO_ID = "id";
    public static final String APP_NAME = "Timetables";

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    private volatile EventAdmin eventAdmin;

    public DemoApplication() {
        super("demoapp", new ApiVersion(1));
    }

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL)
    public void setRepositories(Repositories repos) {
        super.setRepositories(repos);
    }

    public void unsetRepositories(Repositories repo) {
        super.setRepositories(null);
    }



    @Override
    protected void attach() {
        super.attach();
        router.attach(new RouteBuilder("/Timetables/{id}", TimetableResourceGen.class));
        router.attach(new RouteBuilder("/Timetables/", PostTimetableResourceGen.class));
        router.attach(new RouteBuilder("/Timetables/{id}/", PutTimetableResourceGen.class));
        router.attach(new RouteBuilder("/Timetables", TimetablesResourceGen.class));
        router.attach(new RouteBuilder("", TimetablesResourceGen.class));

    }

    public EventAdmin getEventAdmin() {
        return eventAdmin;
    }

}