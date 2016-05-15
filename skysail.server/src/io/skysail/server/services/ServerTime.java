package io.skysail.server.services;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.event.EventAdmin;

import io.skysail.server.EventHelper;
import lombok.extern.slf4j.Slf4j;

//@Component(immediate = true)
@Slf4j
public class ServerTime {

    private EventAdmin eventAdmin;

    private volatile TimerTask timerTask;

    public class MyTimerTask extends TimerTask {

        private EventHelper eventHelper;
        private String currentTime;
        private int msgId;

        public MyTimerTask(EventAdmin eventAdmin) {
            eventHelper = new EventHelper(eventAdmin);
        }

        @Override
        public void run() {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            if (!sdf.format(new Date()).equals(currentTime)) {
                currentTime = sdf.format(new Date());
                msgId = eventHelper.channel(EventHelper.GUI_MSG).info(currentTime).lifetime(1000 * 60L).fire();
            }
        }
    }

    @Activate
    public void activate() {
        log.debug("activating ServerTime task");
        timerTask = new MyTimerTask(eventAdmin);
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(timerTask, 10000, 10*1000);
    }

    @Deactivate
    public void deactivate() {
        log.info("deactivating ServerTime task");
        timerTask.cancel();
        timerTask = null;
    }

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY)
    public void setEventAdmin(EventAdmin eventAdmin) {
        this.eventAdmin = eventAdmin;
    }

    public void unsetEventAdmin(EventAdmin eventAdmin) {
        eventAdmin = null; // NOSONAR
    }


}
