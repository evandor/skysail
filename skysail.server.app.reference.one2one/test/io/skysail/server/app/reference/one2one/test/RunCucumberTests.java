package io.skysail.server.app.reference.one2one.test;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
        //features
        plugin = {"pretty", "json:generated/cucumber.json"}
)
public class RunCucumberTests { // NOSONAR

}
