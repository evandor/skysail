package io.skysail.core.app;

import java.util.List;

import io.skysail.core.resources.SkysailServerResource;

@org.osgi.annotation.versioning.ConsumerType
public interface ApplicationProvider extends Comparable<ApplicationProvider> {

    SkysailApplication getApplication();
    
    <T extends SkysailServerResource<?>> List<String> getTemplatePaths(Class<T> cls);

}
