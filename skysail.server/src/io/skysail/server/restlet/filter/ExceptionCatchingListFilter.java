package io.skysail.server.restlet.filter;

import java.util.List;

import org.restlet.resource.ResourceException;

import io.skysail.core.app.SkysailApplication;
import io.skysail.core.resources.SkysailServerResource;
import io.skysail.domain.Entity;
import io.skysail.server.restlet.filter.helper.ExceptionCatchingFilterHelper;
import io.skysail.server.restlet.response.Wrapper;
import io.skysail.server.restlet.response.Wrapper2;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExceptionCatchingListFilter extends AbstractListResourceFilter {

	private SkysailApplication application;

	public ExceptionCatchingListFilter(SkysailApplication application) {
		this.application = application;
	}

	@Override
	public FilterResult doHandle(SkysailServerResource<?> resource, Wrapper2 responseWrapper) {
		log.debug("entering {}#doHandle", this.getClass().getSimpleName());
		try {
			super.doHandle(resource, responseWrapper);
		} catch (ResourceException re) {
			throw re;
		} catch (Exception e) {
			// ExceptionCatchingFilterHelper.handleError(e, application,
			// responseWrapper, resource.getClass());
			log.error(e.getMessage(), e);
		}
		return FilterResult.CONTINUE;
	}

	@Override
	protected void afterHandle(SkysailServerResource<?> resource, Wrapper2 responseWrapper) {
		resource.getServerInfo().setAgent("Skysail-Server/0.0.1 " + resource.getServerInfo().getAgent());
	}
}
