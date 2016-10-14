package io.skysail.server.app.starmoney.camel;

import org.apache.camel.builder.RouteBuilder;

import io.skysail.server.app.starmoney.ImportProcessor;
import io.skysail.server.app.starmoney.SanitizerProcessor;
import io.skysail.server.app.starmoney.StarMoneyRepository;
import io.skysail.server.domain.jvm.JavaApplicationModel;

public class ImportCsvRoute extends RouteBuilder {

    private StarMoneyRepository repository;
    private JavaApplicationModel applicationModel;

    public ImportCsvRoute(StarMoneyRepository repository, JavaApplicationModel applicationModel) {
        this.repository = repository;
        this.applicationModel = applicationModel;
    }

    @Override
    public void configure() throws Exception {
        from("file:///tmp/in") // ?noop=true
        .process(new SanitizerProcessor())
        .to("file:///tmp/out")
        .process(new ImportProcessor(repository, applicationModel))
        .to("file:///tmp/out2");

    }

}
