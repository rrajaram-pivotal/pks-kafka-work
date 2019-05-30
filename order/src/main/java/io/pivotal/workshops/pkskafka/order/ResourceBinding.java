package io.pivotal.workshops.pkskafka;

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
	 *  KTable for reading messages from the order-event-topic 
	 */
	@Input (ORDER_STREAM_IN)
	KTable<String, Order> orderStreamIn();
	
	@Input (ORDER_VALIDATION_STREAM_IN)
	KStream<String, OrderValidation> orderValidationStreamIn();
	
	
	//@Input (ORDER_IN)
	//KTable<String, Order> orderIn();
	

}
