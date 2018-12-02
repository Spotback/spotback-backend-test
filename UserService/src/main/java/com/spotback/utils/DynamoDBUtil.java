package com.spotback.utils;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;

public class DynamoDBUtil {
	
	static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withCredentials(new EnvironmentVariableCredentialsProvider())
    .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("https://dynamodb.us-west-2.amazonaws.com", "us-west-2"))
    .build();
	public static void main(String[] args) {
		 DynamoDB dynamoDB = new DynamoDB(client);

	        String tableName = "Movies";

	        try {
//	            System.out.println("Attempting to create table; please wait...");
//	            Table table = dynamoDB.createTable(tableName,
//	                Arrays.asList(new KeySchemaElement("year", KeyType.HASH), // Partition
//	                                                                          // key
//	                    new KeySchemaElement("title", KeyType.RANGE)), // Sort key
//	                Arrays.asList(new AttributeDefinition("year", ScalarAttributeType.N),
//	                    new AttributeDefinition("title", ScalarAttributeType.S)),
//	                new ProvisionedThroughput(10L, 10L));
//	            table.waitForActive();
//	            System.out.println("Success.  Table status: " + table.getDescription().getTableStatus());
				Table table = dynamoDB.getTable(tableName);
//				JsonObject jo = new JsonObject().put("year",1995).put("title", "Armageddon");

//				Item item = Item.fromJSON(jo.toString());
//				System.out.println(table.putItem(item).getPutItemResult().toString());
//				JsonObject jo = new JsonObject(table.qu.toJSON());

//				System.out.println(jo.toString());
				HashMap<String, AttributeValue> key_to_get =
						new HashMap<String,AttributeValue>();

				key_to_get.put("title", new AttributeValue("Gone with the wind"));

				GetItemRequest request = new GetItemRequest()
							.withKey(key_to_get)
							.withTableName(tableName);

				JsonObject jo = new JsonObject(client.getItem(request).getItem().toString());
				System.out.println(jo.toString());

	        }
	        catch (Exception e) {
//	            System.err.println("Unable to create table: ");
//	            System.err.println(e.getMessage());
//	            e.printStackTrace();
	        }



	}

}
