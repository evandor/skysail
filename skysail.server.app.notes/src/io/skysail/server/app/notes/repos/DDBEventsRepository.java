package io.skysail.server.app.notes.repos;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.osgi.service.cm.ConfigurationException;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.util.TableUtils;

import io.skysail.domain.Identifiable;
import io.skysail.domain.core.ApplicationModel;
import io.skysail.server.app.notes.Event;
import io.skysail.server.ext.aws.AwsConfiguration;
import lombok.extern.slf4j.Slf4j;
/**
 * AWS DynamoDb Store
 *
 */
@Slf4j
public class DDBEventsRepository extends DDBAbstractRepository {

    private static final String CONTENT = "content";
	private static final String TITLE = "title";

	private final static String TABLE_NAME = "event";

    public DDBEventsRepository(AwsConfiguration awsConfig) throws ConfigurationException {
    	super(awsConfig, TABLE_NAME);
    }

    @Override
    public Identifiable findOne(String id) {
        HashMap<String, Condition> scanFilter = new HashMap<>();
        Condition condition = new Condition()
                .withComparisonOperator(ComparisonOperator.GT.toString())
                .withAttributeValueList(new AttributeValue().withN("1985"));
        scanFilter.put("year", condition);
        ScanRequest scanRequest = new ScanRequest(TABLE_NAME).withScanFilter(scanFilter);
        ScanResult scanResult = dynamoDB.scan(scanRequest);
        System.out.println("Result: " + scanResult);
        return new Event();
    }

    @Override
    public Optional<Identifiable> findOne(String identifierKey, String id) {
        return null;
    }
    
    protected void createTableIfNotExisting(String tableName) throws InterruptedException {
        CreateTableRequest createTableRequest = new CreateTableRequest()
                .withTableName(tableName)
                .withKeySchema(
                        new KeySchemaElement().withAttributeName("eventUuid").withKeyType(KeyType.HASH))
                .withAttributeDefinitions(
                        new AttributeDefinition().withAttributeName("eventUuid").withAttributeType(ScalarAttributeType.S))
                .withKeySchema(
                        new KeySchemaElement().withAttributeName("userUuid").withKeyType(KeyType.RANGE))
                .withAttributeDefinitions(
                        new AttributeDefinition().withAttributeName("userUuid").withAttributeType(ScalarAttributeType.S))
                .withProvisionedThroughput(
                        new ProvisionedThroughput().withReadCapacityUnits(1L).withWriteCapacityUnits(1L));

        try {
        	TableUtils.createTableIfNotExists(dynamoDB, createTableRequest);
        	TableUtils.waitUntilActive(dynamoDB, tableName);
        } catch (Exception e) {
        	log.warn("Problem accessing or using AWS", e);
        }
    }

    @Override
    //    http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/dynamodbv2/datamodeling/DynamoDBMapper.html
    public Object save(Identifiable identifiable, ApplicationModel applicationModel) {
        Event event = (Event)identifiable;
//        Map<String, AttributeValue> item = newItem(event.getUuid(), event.getTitle(), event.getContent());
//        PutItemRequest putItemRequest = new PutItemRequest(TABLE_NAME, item);
//        return dynamoDB.putItem(putItemRequest);
        
        
        DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);
        Long hashKey = 105L;
        double rangeKey = 1.0d;
        //Event obj = mapper.load(TestClass.class, hashKey, rangeKey);
        //obj.getIntegerAttribute().add(42);
        mapper.save(event);
        return null;
    }

    @Override
    public Object update(Identifiable entity, ApplicationModel applicationModel) {
//        Event Event = (Event)entity;
//        Map<String, AttributeValue> item = newItem(Event.getUuid(), Event.getTitle(), Event.getContent());
//        item.remove("EventUuid");
//
//
//        Map<String, AttributeValueUpdate> attributes = new HashMap<>();
//
//        attributes.put(TITLE,   new AttributeValueUpdate().withValue(new AttributeValue(Event.getTitle())).withAction("PUT"));
//        attributes.put(CONTENT, new AttributeValueUpdate().withValue(new AttributeValue(Event.getContent())).withAction("PUT"));
//
//        Map<String, AttributeValue> key = new HashMap<>();
//        key.put("EventUuid", new AttributeValue(Event.getUuid()));
//
//        UpdateItemRequest updateItemRequest = new UpdateItemRequest(TABLE_NAME, key, attributes);
        return null;// dynamoDB.updateItem(updateItemRequest);
    }



    @Override
    public void delete(Identifiable identifiable) {
//        Event Event = (Event)identifiable;
//        Map<String, AttributeValue> key = new HashMap<>();
//        key.put("EventUuid", new AttributeValue(Event.getUuid()));
//        DeleteItemRequest deleteItemRequest = new DeleteItemRequest(TABLE_NAME, key);
//        dynamoDB.deleteItem(deleteItemRequest);
    }

    private static Map<String, AttributeValue> newItem(String id, String title, String content) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("EventUuid", new AttributeValue(id));
        item.put(TITLE, new AttributeValue(title));
        item.put(CONTENT, new AttributeValue(content));
        return item;
    }

    public List<Event> findAll() {
         ScanResult scanned = dynamoDB.scan(TABLE_NAME, Arrays.asList("EventUuid", TITLE, CONTENT));
         return scanned.getItems().stream()
             .map(item -> {
                 Event Event = new Event();
                 //Event.setContent(item.get(CONTENT).getS());
                 return Event;
             }).collect(Collectors.toList());
    }



}