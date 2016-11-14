package io.skysail.server.ext.apns;

import java.util.HashMap;
import java.util.Map;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;

public class Example {

    public static void main(String[] args) {
       // String device_token = "YOUR_DEVICE_TOKEN";
        ApnsService service = APNS.newService()
                .withCert("/home/carsten/apple/certificates", "PASSWORD_OF_CERTIFICATE")
                .withSandboxDestination()
                .build();
        Map<String, ?> map = new HashMap<>();
        String payload = APNS.newPayload()
                //.customFields(map)
                .alertBody("Hello, it's working")
                .sound("default")
                .build();

        service.push(token, payload);
    }

}