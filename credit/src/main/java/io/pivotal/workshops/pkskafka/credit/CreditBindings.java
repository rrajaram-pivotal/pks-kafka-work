package io.pivotal.workshops.pkskafka.credit;


import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.stereotype.Component;

import io.pivotal.workshops.pkskafka.domain.events.order.Order;
import io.pivotal.workshops.pkskafka.domain.events.ordervalidation.OrderValidation;

@Component
public interface CreditBindings {
	

	
	String  ORDER_VALIDATION_STREAM_OUT = "ordervalidationstreamout";
	String  ORDER_STREAM_IN = "orderstreamin";
	
	String ORDER_STORE = "order-store";
	
	

	@Output (ORDER_VALIDATION_STREAM_OUT)
	KStream<String, OrderValidation> orderValidationStreamOut();
	
	@Input (ORDER_STREAM_IN)
	KStream<String, Order> orderStreamIn();
		
	


}
