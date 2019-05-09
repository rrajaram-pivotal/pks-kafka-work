package io.pivotal.workshops.pkskafka.inventory.domain;

import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

import io.pivotal.workshops.pkskafka.inventory.domain.events.Order;


/**
 * Represents endpoint bindings to input and message channels 
 * @author rrajaram
 */

@Component
public interface InventoryBindings {

	String ORDER_IN = "orderin";
	String INVENTORY_IN = "inventoryin";
	String INVENTORY_OUT="inventoryout";
	
	/**
	 *  Kafka Streams input channel for consuming messages from the orders topic. 
	 */
	@Input (ORDER_IN)
	KStream<String, Order> ordersIn();
	
	
	/**
	 *  Kafka Streams input channel for consuming messages from the warehouse-inventory topic. 
	 */
	@Input (INVENTORY_IN)
	KTable<String, Order> inventoryIn();	
	
	
	/**
	 *  Kafka Streams input channel for consuming messages from the warehouse-inventory topic. 
	 */
	@Output (INVENTORY_OUT)
	MessageChannel inventoryOut();
	
}
