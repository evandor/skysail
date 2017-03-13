package io.skysail.core.app;

import java.util.List;

import io.skysail.core.SkysailComponent;

@org.osgi.annotation.versioning.ProviderType
public interface ApplicationListProvider {

    List<SkysailApplication> getApplications();

    void attach(SkysailComponent skysailComponent);

    void detach(SkysailComponent skysailComponent);
}
