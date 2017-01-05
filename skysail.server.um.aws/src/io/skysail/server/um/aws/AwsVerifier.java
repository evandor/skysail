package io.skysail.server.um.aws;

import org.restlet.security.SecretVerifier;
import org.restlet.security.Verifier;

import lombok.extern.slf4j.Slf4j;

//@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
@Slf4j
public class AwsVerifier extends SecretVerifier implements Verifier {

    private String identityId;

    public AwsVerifier(String identityId) {
        this.identityId = identityId;
    }

    @Override
    public int verify(String arg0, char[] arg1) {
        return 0;
    }

}
