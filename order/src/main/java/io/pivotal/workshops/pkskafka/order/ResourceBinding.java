package io.pivotal.workshops.pkskafka.order;

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
public interface ResourceBinding {
	
	String ORDER_OUT = "orderout";
	
	String ORDER_STREAM_IN = "orderstreamin";

	String ORDER_STREAM_OUT = "orderstreamout";
	
	String ORDER_AGG_STREAM_IN = "orderaggstreamin";
	
	String ORDER_STORE = "order-store";
	
	String  ORDER_VALIDATION_STREAM_IN = "ordervalidationstreamin";
	
	//String ORDER_IN = "orderin";
		
	/**
	 *  Message Channel for writing to the OrderOut Channel that in turn writes the messages to the 
	 * Kafka "Orders" topic 
	 */
	@Output (ORDER_OUT)
	MessageChannel orderOut();
	
	/**
	 *  KStream for writing messages to order-event-topic 
	 */
	@Output (ORDER_STREAM_OUT)
	KStream<String, Order> orderStreamOut();	
	
	/**
	 *  KTable for reading messages from the order-event-topic 
	 */
	@Input (ORDER_STREAM_IN)
	KTable<String, Order> orderStreamIn();
	

	/**
	 *  KStream for reading messages from the order-event-topic 
	 */
	@Input (ORDER_AGG_STREAM_IN)
	KStream<String, Order> orderAggStreamIn();
	
	@Input (ORDER_VALIDATION_STREAM_IN)
	KStream<String, OrderValidation> orderValidationStreamIn();
	
	
	//@Input (ORDER_IN)
	//KTable<String, Order> orderIn();
	

}
