package io.skysail.server.app.resources;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import org.osgi.service.log.LogEntry;

import io.skysail.domain.Entity;
import io.skysail.domain.html.Field;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LogEntryDesc implements Entity {

    private static AtomicLong cnt = new AtomicLong();

    @Field
    private String id;

    @Field
    private Date time;

    @Field
    private int level;

    @Field
    private String message;

    @Field
    private String bundle;

    @Field
    private String service;

    @Field
    private String exception;

    public LogEntryDesc(LogEntry log) {
        id = String.valueOf(cnt.getAndIncrement());
        time = new Date(log.getTime());
        level = log.getLevel();
        message = log.getMessage();
        service = log.getServiceReference() == null ? null : log.getServiceReference().toString();
        bundle = log.getBundle().getSymbolicName();
        exception = log.getException() == null ? null : log.getException().getMessage();
    }

}
