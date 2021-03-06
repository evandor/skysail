package io.skysail.server.model.test;

import java.util.List;

import org.mockito.Mockito;

import io.skysail.core.app.SkysailApplication;
import io.skysail.domain.Entity;
import io.skysail.server.model.test.TestListOfEnumResource.MyEnum;
import io.skysail.server.restlet.resources.ListServerResource;

public class TestListOfEnumResource extends ListServerResource<MyEnum>{

    public enum MyEnum implements Entity {
        A,B,C;

        @Override
        public String getId() {
            return null;
        }

    }

    @Override
    public List<MyEnum> getEntity() {
        return null;
    }
    @Override
    public SkysailApplication getApplication() {
        return Mockito.mock(SkysailApplication.class);
    }

}
