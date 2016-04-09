package io.skysail.server.app.demo;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.event.EventAdmin;
import org.restlet.Restlet;

import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import io.skysail.domain.core.Repositories;
import io.skysail.server.app.ApiVersion;
import io.skysail.server.app.ApplicationConfiguration;
import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.demo.resources.PostTimetableResource;
import io.skysail.server.app.demo.resources.PutTimetableResource;
import io.skysail.server.app.demo.resources.TimetableResource;
import io.skysail.server.app.demo.resources.TimetablesResource;
import io.skysail.server.menus.MenuItemProvider;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
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
    @Override
    public void setRepositories(Repositories repos) {
        super.setRepositories(repos);
    }

    public void unsetRepositories(Repositories repo) {
        super.setRepositories(null);
    }
    
    @Activate
    @Override
    public void activate(ApplicationConfiguration appConfig, ComponentContext componentContext) throws ConfigurationException {
    	super.activate(appConfig, componentContext);
    }

    @Override
    protected void attach() {
        super.attach();

        router.attach(new RouteBuilder("/Timetables/{id}", TimetableResource.class));
        router.attach(new RouteBuilder("/Timetables/", PostTimetableResource.class));
        router.attach(new RouteBuilder("/Timetables/{id}/", PutTimetableResource.class));
        router.attach(new RouteBuilder("/Timetables", TimetablesResource.class));
        router.attach(new RouteBuilder("", TimetablesResource.class));

        // call http://localhost:2015/demoapp/v1/unprotected/times?media=json
        router.attach(new RouteBuilder("/unprotected/times", UnprotectedTimesResource.class).noAuthenticationNeeded());
        router.attach(new RouteBuilder("/unprotected/array", UnprotectedArrayResource.class).noAuthenticationNeeded());

        
        router.attach(createStaticDirectory());     
        
        //router.attach(new RouteBuilder("/client/raml.html", RamlClientResource.class));
        
    }

	public EventAdmin getEventAdmin() {
        return eventAdmin;
    }
	
	@Override
	public synchronized Restlet createInboundRoot() {
		// TODO Auto-generated method stub
		return super.createInboundRoot();
	}

}