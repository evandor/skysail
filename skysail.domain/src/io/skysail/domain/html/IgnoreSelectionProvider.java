package io.skysail.domain.html;

import java.util.Collections;
import java.util.Map;

public class IgnoreSelectionProvider implements SelectionProvider {

    @Override
    public Map<String, String> getSelections() {
        return Collections.emptyMap();
    }

    @Override
    public void setConfiguration(Object osgiServicesProvider) {
        // nothing to be done
    }

//    @Override
//    public void setResource(Resource resource) {
//        // nothing to be done
//    }

}
