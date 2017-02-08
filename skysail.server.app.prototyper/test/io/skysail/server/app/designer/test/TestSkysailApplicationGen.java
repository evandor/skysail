package io.skysail.server.app.designer.test;

import java.util.Arrays;

import io.skysail.core.app.SkysailApplication;
import io.skysail.server.app.ApiVersion;

public class TestSkysailApplicationGen extends SkysailApplication {
    
    public TestSkysailApplicationGen() {
        super("testapplication", new ApiVersion(1), Arrays.asList());
    }

}
