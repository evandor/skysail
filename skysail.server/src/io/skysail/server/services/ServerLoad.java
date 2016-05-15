package io.skysail.server.services;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.Timer;
import java.util.TimerTask;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.event.EventAdmin;

import io.skysail.server.EventHelper;
import lombok.extern.slf4j.Slf4j;

@Component(immediate = true)
@Slf4j
public class ServerLoad {

    private EventAdmin eventAdminRef;

    private volatile TimerTask timerTask;

    public class ServerLoadTimerTask extends TimerTask {

        private EventHelper eventHelper;
        private OperatingSystemMXBean operatingSystemMXBean;

        public ServerLoadTimerTask(EventAdmin eventAdmin) {
            eventHelper = new EventHelper(eventAdmin);
            operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        }

        @Override
        public void run() {
            double serverLoad = operatingSystemMXBean.getSystemLoadAverage();
            eventHelper.channel(EventHelper.GUI_PEITY_BAR).info(Double.toString(serverLoad)).fire();
        }
    }

    @Activate
    public void activate() {
        log.debug("activating ServerLoad task");
        timerTask = new ServerLoadTimerTask(eventAdminRef);
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(timerTask, 10000, 60 * 1000);
    }

    @Deactivate
    public void deactivate() {
        log.info("deactivating ServerLoad task");
        timerTask.cancel();
        timerTask = null;
    }

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY)
    public void setEventAdmin(EventAdmin eventAdmin) {
        this.eventAdminRef = eventAdmin;
    }

    public void unsetEventAdmin(EventAdmin eventAdmin) {
        eventAdmin = null;
    }

}
