package io.skysail.server.app.ref.singleentity;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
        //features
        //tags = {"@localtests"},
        tags = {"@JustMe"},
        //tags = {"@singleEntityRefApplication"},
        glue   = { "io.skysail.server.app.ref.singleentity" },
        plugin = { "pretty", "json:generated/cucumber.json", "html:generated/cucumber" }
)
public class RunCucumberTests { // NOSONAR

}
