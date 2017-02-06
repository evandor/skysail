package io.skysail.server.app.notes.repos.hystrix;

import java.util.Arrays;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

public class FindAllCommand  extends HystrixCommand<ScanResult> {

	private AmazonDynamoDBClient dbClient;
    private String tableName;

    public FindAllCommand(AmazonDynamoDBClient dbClient, String tableName) {
        super(HystrixCommandGroupKey.Factory.asKey("AWS"));
        this.dbClient = dbClient;
        this.tableName = tableName;
    }

	@Override
	protected ScanResult run() throws Exception {
		return dbClient.scan(tableName, Arrays.asList("EventUuid", "entity", "type", "tstamp"));
	}

}
