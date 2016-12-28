package io.skysail.server.codegen;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

@Component(immediate = true)
public class InMemoryCodeGenerator implements CodeGenerator {

    public InMemoryCodeGenerator() {
        System.out.println("hier");
    }
    
    @Activate
    public void activate() {
        System.out.println("hier2");
    }
}
