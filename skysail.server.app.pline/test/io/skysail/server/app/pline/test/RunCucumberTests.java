package io.skysail.server.app.pline.test;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
        //features
        //tags = {"@backlog"},
        //tags = {"@JustMe"},
        tags = {"@plineBackend"},
        plugin = {"pretty", "json:generated/cucumber.json"}
)
public class RunCucumberTests { // NOSONAR

}
