package io.skysail.server.app.notes.repos;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.event.EventAdmin;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.util.TableUtils;

import io.skysail.domain.Identifiable;
import io.skysail.domain.core.ApplicationModel;
import io.skysail.server.app.notes.domain.AwsNote;
import io.skysail.server.app.notes.domain.Note;
import io.skysail.server.executors.SkysailExecutorService;
import io.skysail.server.ext.aws.AwsConfiguration;
import lombok.extern.slf4j.Slf4j;

/**
 * AWS DynamoDb Store
 *
 */
@Slf4j
public class AwsNotesRepository extends DDBAbstractRepository {

    private static final String CONTENT = "content";
    private static final String TITLE = "title";

    private static final String TABLE_NAME = "notes";

    public AwsNotesRepository(AwsConfiguration awsConfig, SkysailExecutorService executor, EventAdmin eventAdmin)
            throws ConfigurationException {
        super(awsConfig, TABLE_NAME, eventAdmin);
    }


    @Override
    public Identifiable findOne(String id) {
//        QueryRequest request = new QueryRequest(TABLE_NAME);
//        Condition value = new Condition();
//        value.setComparisonOperator(ComparisonOperator.EQ);
//        Collection<AttributeValue> attributeValueList = new ArrayList<>();
//        attributeValueList.add(new AttributeValue(id));
//        value.setAttributeValueList(attributeValueList);
//        request.addQueryFilterEntry("noteUuid", value);
//        QueryResult query = dbClient.query(request);
//        query.
        return new AwsNote();
    }

    @Override
    public Optional<Identifiable> findOne(String identifierKey, String id) {
        return null;
    }

    @Override
    protected void createTableIfNotExisting(String tableName) throws InterruptedException {
        CreateTableRequest createTableRequest = new CreateTableRequest().withTableName(tableName)
                .withKeySchema(new KeySchemaElement().withAttributeName("noteUuid").withKeyType(KeyType.HASH))
                .withAttributeDefinitions(new AttributeDefinition().withAttributeName("noteUuid")
                        .withAttributeType(ScalarAttributeType.S))
//                .withKeySchema(new KeySchemaElement().withAttributeName("userUuid").withKeyType(KeyType.RANGE))
//                .withAttributeDefinitions(new AttributeDefinition().withAttributeName("userUuid")
//                        .withAttributeType(ScalarAttributeType.S))
                .withProvisionedThroughput(
                        new ProvisionedThroughput().withReadCapacityUnits(1L).withWriteCapacityUnits(1L));
        try {
            TableUtils.createTableIfNotExists(dbClient, createTableRequest);
            TableUtils.waitUntilActive(dbClient, tableName);
        } catch (Exception e) {
            log.warn("Problem accessing or using AWS: {}", e.getMessage());
        }
    }

    @Override
    // http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/dynamodbv2/datamodeling/DynamoDBMapper.html
    public Object save(Identifiable identifiable, ApplicationModel applicationModel) {
        AwsNote event = (AwsNote) identifiable;
        DynamoDBMapper mapper = new DynamoDBMapper(dbClient);
        mapper.save(event);
        return null;
    }

    @Override
    public Object update(Identifiable entity, ApplicationModel applicationModel) {
        AwsNote event = (AwsNote) entity;
        DynamoDBMapper mapper = new DynamoDBMapper(dbClient);
        mapper.save(event);
        return null;
    }

    @Override
    public void delete(Identifiable identifiable) {
         Note note = (Note)identifiable;
         Map<String, AttributeValue> key = new HashMap<>();
         key.put("noteUuid", new AttributeValue(note.getUuid()));

         //DynamoDBMapper mapper = new DynamoDBMapper(dbClient);
         //mapper.delete(findOne(note.getUuid()));

         DynamoDB dynamoDB = new DynamoDB(dbClient);

         Table table = dynamoDB.getTable(TABLE_NAME);

         try {

             DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
             .withPrimaryKey("noteUuid", note.getUuid());
//             .withConditionExpression("#ip = :val")
//             .withNameMap(new NameMap()
//                 .with("#ip", "InPublication"))
//             .withValueMap(new ValueMap()
//             .withBoolean(":val", false))
//             .withReturnValues(ReturnValue.ALL_OLD);

             DeleteItemOutcome outcome = table.deleteItem(deleteItemSpec);

             // Check the response.
             System.out.println("Printing item that was deleted...");
             System.out.println(outcome.getItem().toJSONPretty());

         } catch (Exception e) {
             System.err.println("Error deleting item in ");
             System.err.println(e.getMessage());
         }
    }

    private static Map<String, AttributeValue> newItem(String id, String title, String content) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("EventUuid", new AttributeValue(id));
        item.put(TITLE, new AttributeValue(title));
        item.put(CONTENT, new AttributeValue(content));
        return item;
    }

    public List<AwsNote> findAll() {
        Map<String, Condition> scanFilter = new HashMap<>();
        Condition value = new Condition();
        scanFilter.put("tstamp", value);

        ScanResult scanned = dbClient.scan(TABLE_NAME, Arrays.asList("noteUuid", "entity", "tstamp"));
        return scanned.getItems().stream().map(item -> {
            AwsNote event = new AwsNote();
            event.setEntity(item.get("entity").getS());
            //event.setType(item.get("type").getS());
            event.setTstamp(Long.parseLong(item.get("tstamp").getN()));
            return event;
        }).collect(Collectors.toList());
    }

}