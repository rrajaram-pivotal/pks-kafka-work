package io.pivotal.workshops.pkskafka.analytics.bindings;

import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

import io.pivotal.workshops.pkskafka.analytics.events.OrderEvent;

/**
 * Represents endpoint bindings to input and message channels 
 * @author rrajaram
 */
@Component
public interface AnalyticsBindings {
	
	String ORDER_OUT = "orderout";
	
	String ORDER_IN = "orderin";
	
	String PRODUCT_COUNT_MV="pcmv";
	
	String PRODUCT_COUNT_OUT = "pcout";
	
	String PRODUCT_COUNT_IN = "pcin";
	
	/**
	 *  Kafka Streams for consuming messages from the OrderIn Input Channel. 
	 */
	@Input (ORDER_IN)
	KStream<String, OrderEvent> ordersIn();
	
	/**
	 *  Message Channel for writing to the OrderOut Channel that in turn writes the messages to the 
	 * Kafka "Orders" topic 
	 */
	@Output (ORDER_OUT)
	MessageChannel orderOut();

	
	/**
	 *  Kafka Streams for writing product counts grouped by a time window from the OrderEventProcessor. 
	 */
	@Output (PRODUCT_COUNT_OUT)
	KStream<String, Long> productCountOut();
	
	
	/**
	 *  Kafka Table for reading product counts  
	 */
	@Input (PRODUCT_COUNT_IN)
	KTable <String, Long> productCountIn();
	
	
	
	
}
