package io.pivotal.order.config;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

@Component
public interface KafkaOrderStreamBinding {
	
	String ORDER_OUT = "orderout";
//	String ORDER_IN = "orderin";
	
	
/*	*//**
	 *  Kafka Streams for consuming messages from the OrderIn Input Channel. 
	 *//*
	@Input (ORDER_IN)
	KStream<String, Order> orderIn();*/
	
	/**
	 *  Message Channel for writing to the OrderOut Channel that in turn writes the messages to the 
	 * Kafka "Orders" topic 
	 */
	@Output (ORDER_OUT)
	MessageChannel orderOut();


}
