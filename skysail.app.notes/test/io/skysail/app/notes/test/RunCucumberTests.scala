package io.skysail.app.notes.test


import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(classOf[Cucumber])
@CucumberOptions(
      tags = Array("@notesApplication"),
      glue   = Array("io.skysail.app.notes.test"),
      plugin = Array("pretty", "json:generated/cucumber.json", "html:generated/cucumber")
)
class RunCucumberTests { }
