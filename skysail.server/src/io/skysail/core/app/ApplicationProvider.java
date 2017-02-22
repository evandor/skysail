package io.skysail.core.app;

import java.util.List;

import org.restlet.resource.ServerResource;

@org.osgi.annotation.versioning.ConsumerType
public interface ApplicationProvider extends Comparable<ApplicationProvider> {

    SkysailApplication getApplication();

    <T extends ServerResource> List<String> getTemplatePaths(Class<T> cls);

}
