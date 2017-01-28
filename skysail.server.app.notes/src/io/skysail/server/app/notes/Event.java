package io.skysail.server.app.notes;

import java.util.UUID;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import io.skysail.domain.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@DynamoDBTable(tableName = "event")
public class Event implements Entity {

    private String id;

    @DynamoDBHashKey(attributeName = "eventUuid")
	@DynamoDBAutoGeneratedKey
	private UUID eventUuid;

	@DynamoDBAttribute(attributeName="userUuid")
	private String userUuid;

	@DynamoDBAttribute(attributeName="entity")
	private String entity;

	@DynamoDBAttribute(attributeName="tstamp")
	private long tstamp;
	
	@DynamoDBAttribute(attributeName="type")
	private String type;


}