package io.skysail.server.ext.aws;

import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Amazon AWS Config")
public @interface AwsConfigDescriptor {

    String awsProfileName() default "default";

    String awsRegion() default "US_EAST_1";

    String awsAccountId();

    String awsCognitoIdentityPool();


}
