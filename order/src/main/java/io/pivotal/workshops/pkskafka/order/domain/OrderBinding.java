package io.pivotal.workshops.pkskafka.order.domain;

import org.apache.kafka.streams.kstream.GlobalKTable;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

import io.pivotal.workshops.pkskafka.order.domain.events.Order;

@Component
public interface OrderBinding {
	
	String ORDER_OUT = "orderout";
	
	String ORDER_IN = "orderin";
	
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
	


}
