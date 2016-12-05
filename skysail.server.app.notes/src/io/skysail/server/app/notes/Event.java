package io.skysail.server.app.notes;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBVersionAttribute;

import io.skysail.domain.Identifiable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Event implements Identifiable {

	public enum EventType {
		CREATE, UPDATE, DELETE
	}

    private String id;

	@DynamoDBHashKey
	private Long key;

	@DynamoDBVersionAttribute
	private String userUuid;

	@DynamoDBVersionAttribute
	private EventType type;


}