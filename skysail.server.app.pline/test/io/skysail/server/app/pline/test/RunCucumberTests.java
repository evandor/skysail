package io.skysail.server.app.pline.test;

import cucumber.api.CucumberOptions;

// TODO
//@RunWith(Cucumber.class)
@CucumberOptions(
        //features
        //tags = {"@backlog"},
        //tags = {"@JustMe"},
        tags = {"@plineBackend"},
        plugin = {"pretty", "json:generated/cucumber.json"}
)
public class RunCucumberTests { // NOSONAR

}
