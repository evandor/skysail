package io.skysail.server.app.starmoney.camel;

import org.apache.camel.builder.RouteBuilder;

import io.skysail.server.app.starmoney.repos.AccountsInMemoryRepository;
import io.skysail.server.app.starmoney.repos.DbAccountRepository;
import io.skysail.server.domain.jvm.SkysailApplicationModel;

public class ImportCsvRoute extends RouteBuilder {

    private DbAccountRepository dbRepository;
    private SkysailApplicationModel applicationModel;
    private AccountsInMemoryRepository csvRepository;

    public ImportCsvRoute(DbAccountRepository repository, AccountsInMemoryRepository csvRepo, SkysailApplicationModel applicationModel) {
        this.dbRepository = repository;
        this.csvRepository = csvRepo;
        this.applicationModel = applicationModel;
    }

    @Override
    public void configure() throws Exception {
        from("file:///Users/carsten/tmp/in?noop=true") //
        .process(new SanitizerProcessor())
        .to("file:///Users/carsten/tmp/out")
        .process(new Import2MemoryProcessor(dbRepository, csvRepository, applicationModel))
        .to("file:///Users/carsten/tmp/out2");

    }

}
