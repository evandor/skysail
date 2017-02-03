package io.skysail.server.app.designer.test;

import java.util.Arrays;

import io.skysail.server.app.ApiVersion;
import io.skysail.server.app.SkysailApplication;

public class TestSkysailApplicationGen extends SkysailApplication {
    
    public TestSkysailApplicationGen() {
        super("testapplication", new ApiVersion(1), Arrays.asList());
    }

}
