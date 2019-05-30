package io.pivotal.workshops.pkskafka.inventory;


import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

import io.pivotal.workshops.pkskafka.domain.events.inventory.Inventory;
import io.pivotal.workshops.pkskafka.domain.events.order.Order;
import io.pivotal.workshops.pkskafka.domain.events.ordervalidation.OrderValidation;

@Component
public interface InventoryBindings {
	

	
	String  ORDER_VALIDATION_STREAM_OUT = "ordervalidationstreamout";
	String  ORDER_VALIDATION_STREAM_IN = "ordervalidationstreamin";
	String  ORDER_STREAM_IN = "orderstreamin";
	
	//String  UPDATE_INV_ORDER_STREAM_IN = "updinvorderstreamin";
	
	@Input (ORDER_VALIDATION_STREAM_IN)
	KStream<String, OrderValidation> orderValidationStreamIn();	
	
	@Output (ORDER_VALIDATION_STREAM_OUT)
	KStream<String, OrderValidation> orderValidationStreamOut();

	
	@Input (ORDER_STREAM_IN)
	KStream<String, Order> orderStreamIn();	
	
	//@Input (UPDATE_INV_ORDER_STREAM_IN)
	//KStream<String, Order> updInvOrderStreamIn();

	
	String INVENTORY_IN = "inventoryin";
	String INVENTORY_OUT="inventoryout";
	String INVENTORY_STREAM_OUT="inventorystreamout";
	String INVENTORY_STORE="inventory-store";
	
	/**
	 *  GlobalKTable input channel for consuming messages from the warehouse-inventory topic. 
	 */
	@Input (INVENTORY_IN)
	GlobalKTable<String, Inventory> inventoryIn();	
	
	
	/**
	 *  Kafka Streams input channel for consuming messages from the warehouse-inventory topic. 
	 */
	@Output (INVENTORY_OUT)
	MessageChannel inventoryOut();
	
	/**
	 * Output Channel to Inventory Stream
	 * @return
	 */
	//@Output (INVENTORY_STREAM_OUT)
	//KStream<String, Inventory> inventoryStreamOut();	


}
