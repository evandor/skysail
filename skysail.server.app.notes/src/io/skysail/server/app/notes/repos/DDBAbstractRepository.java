package io.skysail.server.app.notes.repos;

import org.osgi.service.cm.ConfigurationException;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.util.TableUtils.TableNeverTransitionedToStateException;

import io.skysail.domain.Identifiable;
import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.ext.aws.AwsConfiguration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class DDBAbstractRepository implements DbRepository {
	
    protected AmazonDynamoDBClient dbClient;

    // Need to remove this being called from activator!
    public DDBAbstractRepository(AwsConfiguration awsConfig, String tableName) throws ConfigurationException {
        init(awsConfig);
        try {
            createTableIfNotExisting(tableName);
        } catch (TableNeverTransitionedToStateException | InterruptedException e) {
            throw new RuntimeException("AWS call failed", e); // NOSONAR
        }
	}
    
    protected abstract void createTableIfNotExisting(String tableName) throws InterruptedException;

	private void init(AwsConfiguration awsConfig) throws ConfigurationException {
        AWSCredentials credentials = null;
        try {
            String profileName = awsConfig.getConfig().awsProfileName();
			credentials = new ProfileCredentialsProvider(profileName).getCredentials();
        } catch (Exception e) {
            String errorMsg = "Cannot load the credentials from the credential profiles file. "
                    + "Please make sure that your credentials file is at the correct "
                    + "location (<username>/.aws/credentials), and is in valid format.";
            log.error(errorMsg);
			throw new ConfigurationException(errorMsg, "awsProfileName", e);
        }
        dbClient = new AmazonDynamoDBClient(credentials);
        Region region = Region.getRegion(Regions.US_EAST_1);
        dbClient.setRegion(region);
    }

	

	@Override
    public Class<? extends Identifiable> getRootEntity() {
        return null;
    }



}
