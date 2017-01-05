package io.skysail.server.app.notes.service;

import java.net.SocketException;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.amazonaws.services.sns.model.UnsubscribeRequest;

import lombok.extern.slf4j.Slf4j;

@Component(immediate = true)
@Slf4j
public class AwsSnsService {

    private static final String ARN_AWS_SNS_SKYSAIL_SERVER_APP_NOTES = "arn:aws:sns:us-east-1:918943519227:skysail-server-app-notes";
    private String endpoint;
    private AmazonSNSClient snsClient;

    public AwsSnsService() throws SocketException {
        String ip;
        try {
            //ip = InetAddress.getLocalHost();

//            try(final DatagramSocket socket = new DatagramSocket()){
//                socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
//                ip = socket.getLocalAddress().getHostAddress();
//              }
            endpoint = "http://80.142.214.254:2020/notes/v1/sync";
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Activate
    public void activate() {
        if (endpoint == null) {
            return;
        }
        String profileName = "admin";
        //snsClient = new AmazonSNSClient(new ClasspathPropertiesFileCredentialsProvider());
        snsClient = new AmazonSNSClient(new ProfileCredentialsProvider(profileName));
        snsClient.setRegion(Region.getRegion(Regions.US_EAST_1));
        log.info("subscribing to endpoint {} for topic {}", endpoint, ARN_AWS_SNS_SKYSAIL_SERVER_APP_NOTES);
        SubscribeRequest subRequest = new SubscribeRequest(ARN_AWS_SNS_SKYSAIL_SERVER_APP_NOTES, "http", endpoint);
        try {
            snsClient.subscribe(subRequest);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        System.out.println("SubscribeRequest - " + snsClient.getCachedResponseMetadata(subRequest));
        System.out.println("Check your email and confirm subscription.");
    }

    @Deactivate
    public void deactivate() {
        if (endpoint == null) {
            return;
        }
        UnsubscribeRequest subRequest = new UnsubscribeRequest(ARN_AWS_SNS_SKYSAIL_SERVER_APP_NOTES);
        try {
            snsClient.unsubscribe(subRequest);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        // get request id for SubscribeRequest from SNS metadata
        System.out.println("SubscribeRequest - " + snsClient.getCachedResponseMetadata(subRequest));
        System.out.println("Check your email and confirm subscription.");
        snsClient = null;
    }



}
