package io.skysail.server.app.sidebar;

import java.util.List;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.BatchGetItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableKeysAndAttributes;

import io.skysail.server.restlet.resources.ListServerResource;

public class SidebarsResource extends ListServerResource<Sidebar> {

	@Override
	public List<Sidebar> getEntity() {
		AmazonDynamoDB db = new AmazonDynamoDBClient();
		Region usEast1 = Region.getRegion(Regions.US_EAST_1);
		db.setRegion(usEast1);

		DynamoDB dynamoDB = new DynamoDB(db);

		Table table = dynamoDB.getTable("sidebar");

		TableKeysAndAttributes forumTableKeysAndAttributes = new TableKeysAndAttributes("sidebar");
		forumTableKeysAndAttributes.addHashOnlyPrimaryKeys("Name", "Amazon S3", "Amazon DynamoDB");

		BatchGetItemOutcome outcome = dynamoDB.batchGetItem(forumTableKeysAndAttributes);

		for (String tableName : outcome.getTableItems().keySet()) {
			System.out.println("Items in table " + tableName);
			List<Item> items = outcome.getTableItems().get(tableName);
			for (Item item : items) {
				System.out.println(item);
			}
		}
		return null;
	}

}
