package io.skysail.api;

import org.jbehave.core.configuration.*;
import org.jbehave.core.junit.*;
import org.jbehave.core.reporters.*;
import org.jbehave.core.io.*;
import org.jbehave.core.steps.*;
import org.junit.Test;

public class ICanToggleACell extends JUnitStory {
 
    // Here we specify the configuration, starting from default MostUsefulConfiguration, and changing only what is needed
    @Override
    public Configuration configuration() {
        return new MostUsefulConfiguration()
            // where to find the stories
            .useStoryLoader(new LoadFromClasspath(this.getClass()))  
            // CONSOLE and TXT reporting
            .useStoryReporterBuilder(new StoryReporterBuilder().withDefaultFormats().withFormats(Format.CONSOLE, Format.TXT)); 
    }
 
    // Here we specify the steps classes
    @Override
    public InjectableStepsFactory stepsFactory() {        
        // varargs, can have more that one steps classes
        return new InstanceStepsFactory(configuration(), new GridSteps());
    }
    
    @Test
    public void run() throws Throwable {  
    	super.run();
    }
}