package io.skysail.server.app.resources;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogReaderService;
import org.osgi.service.log.LogService;

import io.skysail.server.app.SkysailRootApplication;
import io.skysail.server.restlet.resources.ListServerResource;

public class LogsResource extends ListServerResource<LogEntryDesc> {

    private static final int MAX_LOGS = 1000;

    @Override
    public List<LogEntryDesc> getEntity() {
        LogReaderService logReaderService = ((SkysailRootApplication) getApplication()).getLogReaderService();
        List<LogEntryDesc> result = new ArrayList<>();
        int index = 0;
        for (Enumeration logEntries = logReaderService.getLog(); logEntries.hasMoreElements() && index < MAX_LOGS;) {
            LogEntry nextLog = (LogEntry) logEntries.nextElement();
            if (nextLog.getLevel() <= LogService.LOG_DEBUG) {
                // logJson( jw, nextLog, index++, traces );
                result.add(new LogEntryDesc(nextLog));
            }
        }
        return result;
    }

}
