package io.skysail.server.app.notes;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.amazonaws.services.dynamodbv2.util.TableUtils.TableNeverTransitionedToStateException;

import io.skysail.domain.Identifiable;
import io.skysail.domain.core.ApplicationModel;
import io.skysail.domain.core.repos.DbRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * AWS DynamoDb Store
 *
 */
@Slf4j
public class DDBNotesRepository implements DbRepository {

	private final static String NOTES_TABLE_NAME = "notes";

	private AmazonDynamoDBClient dynamoDB;

	public DDBNotesRepository() {
		init();
		try {
			createTableIfNotExisting(NOTES_TABLE_NAME);
		} catch (TableNeverTransitionedToStateException | InterruptedException e) {
			log.error(e.getMessage(), e);
		}
	}

	private void createTableIfNotExisting(String tableName)
			throws TableNeverTransitionedToStateException, InterruptedException {
		CreateTableRequest createTableRequest = new CreateTableRequest()
				.withTableName(tableName)
				.withKeySchema(
						new KeySchemaElement().withAttributeName("name").withKeyType(KeyType.HASH))
				.withAttributeDefinitions(
						new AttributeDefinition().withAttributeName("name").withAttributeType(ScalarAttributeType.S))
				.withProvisionedThroughput(
						new ProvisionedThroughput().withReadCapacityUnits(1L).withWriteCapacityUnits(1L));

		TableUtils.createTableIfNotExists(dynamoDB, createTableRequest);
		TableUtils.waitUntilActive(dynamoDB, tableName);
	}

	private void init() {
		AWSCredentials credentials = null;
		try {
			credentials = new ProfileCredentialsProvider("default").getCredentials();
		} catch (Exception e) {
			throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
					+ "Please make sure that your credentials file is at the correct "
					+ "location (<username>/.aws/credentials), and is in valid format.", e);
		}
		dynamoDB = new AmazonDynamoDBClient(credentials);
		Region region = Region.getRegion(Regions.US_EAST_1);
		dynamoDB.setRegion(region);
	}

	@Override
	public Class<? extends Identifiable> getRootEntity() {
		return null;
	}

	@Override
	public Identifiable findOne(String id) {
		HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
        Condition condition = new Condition()
            .withComparisonOperator(ComparisonOperator.GT.toString())
            .withAttributeValueList(new AttributeValue().withN("1985"));
        scanFilter.put("year", condition);
        ScanRequest scanRequest = new ScanRequest(NOTES_TABLE_NAME).withScanFilter(scanFilter);
        ScanResult scanResult = dynamoDB.scan(scanRequest);
        System.out.println("Result: " + scanResult);
        return new Note();
	}

	@Override
	public Optional<Identifiable> findOne(String identifierKey, String id) {
		return null;
	}

	@Override
	public Object save(Identifiable identifiable, ApplicationModel applicationModel) {
		Map<String, AttributeValue> item = newItem("Bill & Ted's Excellent Adventure", 1989, "****", "James", "Sara");
		PutItemRequest putItemRequest = new PutItemRequest(NOTES_TABLE_NAME, item);
		return dynamoDB.putItem(putItemRequest);
	}

	@Override
	public Object update(Identifiable entity, ApplicationModel applicationModel) {
		return null;
	}

	@Override
	public void delete(Identifiable identifiable) {

	}

	private static Map<String, AttributeValue> newItem(String name, int year, String rating, String... fans) {
		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
		item.put("name", new AttributeValue(name));
		item.put("year", new AttributeValue().withN(Integer.toString(year)));
		item.put("rating", new AttributeValue(rating));
		item.put("fans", new AttributeValue().withSS(fans));

		return item;
	}

}