package io.skysail.server.executors.impl;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

import io.skysail.server.executors.SkysailExecutorService;
import lombok.extern.slf4j.Slf4j;

@Component(immediate = true)
@Slf4j
public class ServerExecutors implements SkysailExecutorService {

    private ExecutorService executor = null;

    @Activate
    public void activate() {
       executor = Executors.newFixedThreadPool(4);
    }

    @Deactivate
    public void deactivate() {
        try {
            log.info("attempt to shutdown executor");
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) { // NOSONAR
            log.error("tasks interrupted");
        } finally {
            if (!executor.isTerminated()) {
                log.error("cancel non-finished tasks");
            }
            executor.shutdownNow();
            log.info("shutdown finished");
        }
        executor = null;
    }

    @Override
    public Future<?> submit(Callable<?> task) {
        log.info("submitting task {}", task.toString());
        return executor.submit(task);
    }
}
