package io.skysail.server.app.notes.repos;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.concurrent.CompletableFuture;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import io.skysail.domain.Entity;
import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.ext.aws.AwsConfiguration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class DDBAbstractRepository implements DbRepository {

    protected AmazonDynamoDBClient dbClient;

    protected EventAdmin eventAdmin;

    public DDBAbstractRepository(AwsConfiguration awsConfig, String tableName, EventAdmin eventAdmin) throws ConfigurationException {
        init(awsConfig,eventAdmin, tableName);
        this.eventAdmin = eventAdmin;
    }

    protected abstract void createTableIfNotExisting(String tableName) throws InterruptedException;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void init(AwsConfiguration awsConfig, EventAdmin eventAdmin, String tableName) throws ConfigurationException {
        initAwsConnection(awsConfig, eventAdmin, tableName)
            .thenApply((String info) -> {
                Dictionary properties = new Hashtable();
                properties.put("time", System.currentTimeMillis());
                Event reportGeneratedEvent = new Event(this.getClass().getName().replace(".", "/") + "/INIT", properties);
                eventAdmin.postEvent(reportGeneratedEvent);
                return null;
            });
    }

    private CompletableFuture<String> initAwsConnection(AwsConfiguration awsConfig, EventAdmin eventAdmin, String tableName) {
        return CompletableFuture.supplyAsync(() -> {

            AWSCredentials credentials = null;
            try {
                String profileName = awsConfig.getConfig().awsProfileName();
                credentials = new ProfileCredentialsProvider(profileName).getCredentials();
            } catch (Exception e) { // NOSONAR
                String errorMsg = "Cannot load the credentials from the credential profiles file. "
                        + "Please make sure that your credentials file is at the correct "
                        + "location (<username>/.aws/credentials), and is in valid format.";
                log.error(errorMsg);
                return errorMsg;
            }
            dbClient = new AmazonDynamoDBClient(credentials);
            Region region = Region.getRegion(Regions.US_EAST_1);
            dbClient.setRegion(region);

            try {
                createTableIfNotExisting(tableName);
            } catch (InterruptedException e) {
                throw new RuntimeException("AWS call failed", e); // NOSONAR
            }

            return "connected to AWS Region " + region;
        });
    }

    @Override
    public Class<? extends Entity> getRootEntity() {
        return null;
    }

}
