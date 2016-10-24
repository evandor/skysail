package io.skysail.server.app;

import java.util.Enumeration;

import org.osgi.service.log.LogListener;
import org.osgi.service.log.LogReaderService;

public class NoOpLogReaderService implements LogReaderService {

	@Override
	public void addLogListener(LogListener listener) {
	}

	@Override
	public void removeLogListener(LogListener listener) {
	}

	@Override
	public Enumeration getLog() {
		return null;
	}

}
