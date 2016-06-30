package io.skysail.server.app.webconsole;

import java.util.List;

import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.webconsole.osgi.entities.bundles.BundleDescriptor;

public class JsonBundlesResource extends ListServerResource<BundleDescriptor> {

    @Override
    public List<BundleDescriptor> getEntity() {
        return ((WebconsoleApplication)getApplication()).getNewOsgiService().getBundleDescriptors();
    }

}
