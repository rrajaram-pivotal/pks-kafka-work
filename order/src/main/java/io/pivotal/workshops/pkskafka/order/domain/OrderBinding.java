package io.pivotal.workshops.pkskafka.order.domain;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

@Component
public interface OrderBinding {
	
	String ORDER_OUT = "orderout";
	
	
	/**
	 *  Message Channel for writing to the OrderOut Channel that in turn writes the messages to the 
	 * Kafka "Orders" topic 
	 */
	@Output (ORDER_OUT)
	MessageChannel orderOut();


}
