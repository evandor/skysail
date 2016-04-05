package io.skysail.server.ext.ramlconsole.webresource;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.event.EventAdmin;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.CacheDirective;
import org.restlet.data.LocalReference;
import org.restlet.data.Status;
import org.restlet.routing.Filter;
import org.restlet.routing.Router;

import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.utils.ClassLoaderDirectory;
import io.skysail.server.utils.CompositeClassLoader;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@org.osgi.service.component.annotations.Component
@Slf4j
public class RamlConsoleWebappApplication extends SkysailApplication implements ApplicationProvider {

    private static final String ROOT = "static"+RamlConsoleConstants.WEB_RESOURCE_NAME;
    
    @Getter
    @org.osgi.service.component.annotations.Reference(cardinality = ReferenceCardinality.OPTIONAL)
    private volatile EventAdmin eventAdmin;
    
    public RamlConsoleWebappApplication() {
        this("webapp serving static files");
        log.info("static resources will be available at {}", "/" + ROOT + "/" + RamlConsoleConstants.WEB_RESOURCE_VERSION + "/...");
    }

    public RamlConsoleWebappApplication(String staticPathTemplate) {
        super(ROOT);
        setName(ROOT);
    }

    
    @Override
    public Restlet createInboundRoot() {
        LocalReference localReference = LocalReference.createClapReference(LocalReference.CLAP_THREAD, "/"+ROOT+"/");

        CompositeClassLoader customCL = new CompositeClassLoader();
        customCL.addClassLoader(Thread.currentThread().getContextClassLoader());
        customCL.addClassLoader(Router.class.getClassLoader());
        customCL.addClassLoader(this.getClass().getClassLoader());

        ClassLoaderDirectory staticDirectory = new ClassLoaderDirectory(getContext(), localReference, customCL);

        Filter cachingFilter = new Filter() {
            @Override
            public Restlet getNext() {
                return staticDirectory;
            }

            @Override
            protected void afterHandle(Request request, Response response) {
                super.afterHandle(request, response);
                if (response.getEntity() != null) {
                    if (request.getResourceRef().toString(false, false).contains("nocache")) {
                        response.getEntity().setModificationDate(null);
                        response.getEntity().setExpirationDate(null);
                        response.getEntity().setTag(null);
                        response.getCacheDirectives().add(CacheDirective.noCache());
                    } else {
                        response.setStatus(Status.SUCCESS_OK);
                        Calendar c = new GregorianCalendar();
                        c.setTime(new Date());
                        c.add(Calendar.DAY_OF_MONTH, 10);
                        response.getEntity().setExpirationDate(c.getTime());
                        response.getEntity().setModificationDate(null);
                        response.getCacheDirectives().add(CacheDirective.publicInfo());
                    }
                }
            }
        };

        Router router = new Router(getContext());
        router.attachDefault(cachingFilter);
        return router;
    }

    @Override
    public SkysailApplication getApplication() {
        return this;
    }

}
