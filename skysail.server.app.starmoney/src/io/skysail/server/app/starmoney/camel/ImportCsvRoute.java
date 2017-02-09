package io.skysail.server.app.starmoney.camel;

import org.apache.camel.builder.RouteBuilder;

import io.skysail.core.model.SkysailApplicationModel;
import io.skysail.server.app.starmoney.config.StarMoneyApplicationConfiguration;
import io.skysail.server.app.starmoney.repos.AccountsInMemoryRepository;
import io.skysail.server.app.starmoney.repos.DbAccountRepository;

public class ImportCsvRoute extends RouteBuilder {

    private DbAccountRepository dbRepository;
    private SkysailApplicationModel applicationModel;
    private AccountsInMemoryRepository csvRepository;
	private StarMoneyApplicationConfiguration config;

    public ImportCsvRoute(DbAccountRepository repository, AccountsInMemoryRepository csvRepo, StarMoneyApplicationConfiguration config, SkysailApplicationModel applicationModel) {
        this.dbRepository = repository;
        this.csvRepository = csvRepo;
		this.config = config;
        this.applicationModel = applicationModel;
    }
    
    @Override
    public void configure() throws Exception {
        from("file://"+config.inputDir()+"?noop=true") //
        .process(new SanitizerProcessor())
        .to("file://" + config.stageDir())
        .process(new Import2MemoryProcessor(dbRepository, csvRepository, applicationModel))
        .to("file://" + config.outputDir());

    }

}
