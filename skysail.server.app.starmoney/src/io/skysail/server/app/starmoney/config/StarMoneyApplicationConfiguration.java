package io.skysail.server.app.starmoney.config;

public @interface StarMoneyApplicationConfiguration {

    String host() default "localhost";

    String inputDir() default ".";
    
    String stageDir() default "./stage";
    
    String outputDir() default "./output";
	
}
