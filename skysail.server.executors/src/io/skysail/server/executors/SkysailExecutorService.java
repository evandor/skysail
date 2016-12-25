package io.skysail.server.executors;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public interface SkysailExecutorService {

    Future<?> submit(Callable<?> task);

}
