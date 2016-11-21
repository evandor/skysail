package io.skysail.server.app.starmoney.camel;

import org.apache.camel.builder.RouteBuilder;

import io.skysail.server.app.starmoney.Import2MemoryProcessor;
import io.skysail.server.app.starmoney.SanitizerProcessor;
import io.skysail.server.app.starmoney.StarMoneyDbRepository;
import io.skysail.server.app.starmoney.StarMoneyInMemoryRepository;
import io.skysail.server.domain.jvm.SkysailApplicationModel;

public class ImportCsvRoute extends RouteBuilder {

    private StarMoneyDbRepository dbRepository;
    private SkysailApplicationModel applicationModel;
    private StarMoneyInMemoryRepository csvRepository;

    public ImportCsvRoute(StarMoneyDbRepository repository, StarMoneyInMemoryRepository csvRepo, SkysailApplicationModel applicationModel) {
        this.dbRepository = repository;
        this.csvRepository = csvRepo;
        this.applicationModel = applicationModel;
    }

    @Override
    public void configure() throws Exception {
        from("file:///tmp/in?noop=true") //
        .process(new SanitizerProcessor())
        .to("file:///tmp/out")
        .process(new Import2MemoryProcessor(dbRepository, csvRepository, applicationModel))
        .to("file:///tmp/out2");

    }

}
