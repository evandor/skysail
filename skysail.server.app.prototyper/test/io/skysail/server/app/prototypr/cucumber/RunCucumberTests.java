package io.skysail.server.app.prototypr.cucumber;

import cucumber.api.CucumberOptions;

//@RunWith(Cucumber.class)
@CucumberOptions(
        //features
//        tags = {"@JustMe"},
 //       tags = {"@applications","@entities","~@Ignore"},
 //       tags = {"@entities"},
//               tags = {"@applications"},
		plugin = {"pretty", "json:generated/cucumber.json"}
)
public class RunCucumberTests { // NOSONAR

}
