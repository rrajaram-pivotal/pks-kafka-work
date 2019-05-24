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

@Component
public interface InventoryBindings {
	

	
	String  ORDER_STREAM_OUT = "orderstreamout";
	String  ORDER_STREAM_IN = "orderstreamin";
	
	String ORDER_STORE = "order-store";
	
	

	@Output (ORDER_STREAM_OUT)
	KStream<String, Order> orderStreamOut();
	
	@Input (ORDER_STREAM_IN)
	KStream<String, Order> orderStreamIn();	

	
	String INVENTORY_IN = "inventoryin";
	String INVENTORY_OUT="inventoryout";
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
		
	


}
