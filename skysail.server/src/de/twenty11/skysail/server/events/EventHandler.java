package de.twenty11.skysail.server.events;

import java.util.HashMap;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

@Component
public class EventHandler {

    private static EventAdmin eventAdmin;

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL)
    public void setEventAdmin(EventAdmin eventAdmin) {
        EventHandler.eventAdmin = eventAdmin;
    }

    public void unsetEventAdmin(EventAdmin eventAdmin) {
        eventAdmin = null;
    }
    
    public static synchronized EventInvocationResult sendEvent(String topic, String msg, String type) {
        if (eventAdmin == null) {
            return EventInvocationResult.EVENT_ADMIN_NOT_AVAILABLE;
        }
        Map<String, String> map = new HashMap<>();
        map.put("msg", msg);
        map.put("type", type);
        eventAdmin.postEvent(new Event(topic, map));
        return EventInvocationResult.SENT;
    }

}
