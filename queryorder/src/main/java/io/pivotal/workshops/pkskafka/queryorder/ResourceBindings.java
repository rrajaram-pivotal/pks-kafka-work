package io.pivotal.workshops.pkskafka.queryorder;

import org.apache.kafka.streams.kstream.KTable;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.stereotype.Component;

import io.pivotal.workshops.pkskafka.domain.events.order.Order;

@Component
public interface ResourceBindings {
	
	String ORDER_STREAM_IN = "orderstreamin";

	String ORDER_STORE = "order-store";
	
	/**
	 *  KTable for reading messages from the order-event-topic 
	 */
	@Input (ORDER_STREAM_IN)
	KTable<String, Order> orderStreamIn();
	

}
