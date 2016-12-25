package io.skysail.server.app.notes.repos;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.osgi.service.cm.ConfigurationException;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteItemRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.amazonaws.services.dynamodbv2.util.TableUtils.TableNeverTransitionedToStateException;

import io.skysail.domain.Identifiable;
import io.skysail.domain.core.ApplicationModel;
import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.app.notes.Note;
import io.skysail.server.ext.aws.AwsConfiguration;
import lombok.extern.slf4j.Slf4j;
/**
 * AWS DynamoDb Store
 *
 */
@Slf4j
public class DDBNotesRepository extends DDBAbstractRepository {

    private static final String CONTENT = "content";
	private static final String TITLE = "title";

	private final static String NOTES_TABLE_NAME = "notes";

    public DDBNotesRepository(AwsConfiguration awsConfig) throws ConfigurationException {
    	super(awsConfig, NOTES_TABLE_NAME, null);
    }

    @Override
    public Identifiable findOne(String id) {
        HashMap<String, Condition> scanFilter = new HashMap<>();
        Condition condition = new Condition()
                .withComparisonOperator(ComparisonOperator.GT.toString())
                .withAttributeValueList(new AttributeValue().withN("1985"));
        scanFilter.put("year", condition);
        ScanRequest scanRequest = new ScanRequest(NOTES_TABLE_NAME).withScanFilter(scanFilter);
        ScanResult scanResult = dbClient.scan(scanRequest);
        System.out.println("Result: " + scanResult);
        return new Note();
    }

    @Override
    public Optional<Identifiable> findOne(String identifierKey, String id) {
        return null;
    }
    
    protected void createTableIfNotExisting(String tableName) throws InterruptedException {
        CreateTableRequest createTableRequest = new CreateTableRequest()
                .withTableName(tableName)
                .withKeySchema(
                        new KeySchemaElement().withAttributeName("noteUuid").withKeyType(KeyType.HASH))
                .withAttributeDefinitions(
                        new AttributeDefinition().withAttributeName("noteUuid").withAttributeType(ScalarAttributeType.S))
                .withProvisionedThroughput(
                        new ProvisionedThroughput().withReadCapacityUnits(1L).withWriteCapacityUnits(1L));

        TableUtils.createTableIfNotExists(dbClient, createTableRequest);
        TableUtils.waitUntilActive(dbClient, tableName);
    }

    @Override
    public Object save(Identifiable identifiable, ApplicationModel applicationModel) {
        Note note = (Note)identifiable;
        Map<String, AttributeValue> item = newItem(note.getUuid(), note.getTitle(), note.getContent());
        PutItemRequest putItemRequest = new PutItemRequest(NOTES_TABLE_NAME, item);
        return dbClient.putItem(putItemRequest);
    }

    @Override
    public Object update(Identifiable entity, ApplicationModel applicationModel) {
        Note note = (Note)entity;
        Map<String, AttributeValue> item = newItem(note.getUuid(), note.getTitle(), note.getContent());
        item.remove("noteUuid");


        Map<String, AttributeValueUpdate> attributes = new HashMap<>();

        attributes.put(TITLE,   new AttributeValueUpdate().withValue(new AttributeValue(note.getTitle())).withAction("PUT"));
        attributes.put(CONTENT, new AttributeValueUpdate().withValue(new AttributeValue(note.getContent())).withAction("PUT"));

        Map<String, AttributeValue> key = new HashMap<>();
        key.put("noteUuid", new AttributeValue(note.getUuid()));

        UpdateItemRequest updateItemRequest = new UpdateItemRequest(NOTES_TABLE_NAME, key, attributes);
        return dbClient.updateItem(updateItemRequest);
    }



    @Override
    public void delete(Identifiable identifiable) {
        Note note = (Note)identifiable;
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("noteUuid", new AttributeValue(note.getUuid()));
        DeleteItemRequest deleteItemRequest = new DeleteItemRequest(NOTES_TABLE_NAME, key);
        dbClient.deleteItem(deleteItemRequest);
    }

    private static Map<String, AttributeValue> newItem(String id, String title, String content) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("noteUuid", new AttributeValue(id));
        item.put(TITLE, new AttributeValue(title));
        item.put(CONTENT, new AttributeValue(content));
        return item;
    }

    public List<Note> findAll() {
         ScanResult scanned = dbClient.scan(NOTES_TABLE_NAME, Arrays.asList("noteUuid", TITLE, CONTENT));
         return scanned.getItems().stream()
             .map(item -> {
                 Note note = new Note();
                 note.setUuid(item.get("noteUuid").getS());
                 note.setTitle(item.get(TITLE).getS());
                 note.setContent(item.get(CONTENT).getS());
                 return note;
             }).collect(Collectors.toList());
    }



}