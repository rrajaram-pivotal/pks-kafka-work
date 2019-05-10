package io.pivotal.workshops.pkskafka;

import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

import io.pivotal.workshops.pkskafka.order.domain.events.Order;

@Component
public interface ResourceBinding {
	
	String ORDER_OUT = "orderout";
	
	String ORDER_IN = "orderin";
	
	String  ORDER_STREAM_OUT = "orderstreamout";
	String  ORDER_STREAM_IN = "orderstreamin";
	
	String ORDER_STORE = "order-store";
		
	/**
	 *  Message Channel for writing to the OrderOut Channel that in turn writes the messages to the 
	 * Kafka "Orders" topic 
	 */
	@Output (ORDER_OUT)
	MessageChannel orderOut();
	
	/**
	 *  Kafka Streams for consuming messages from the OrderIn Input Channel. 
	 */
	@Input (ORDER_IN)
	GlobalKTable<String, Order> ordersIn();
	

	@Output (ORDER_STREAM_OUT)
	KStream<String, Order> orderStreamOut();
	
	@Input (ORDER_STREAM_IN)
	KStream<String, Order> orderStreamIn();	

	
	String INVENTORY_IN = "inventoryin";
	String INVENTORY_OUT="inventoryout";
	
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
