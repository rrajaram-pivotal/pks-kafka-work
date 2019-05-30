package io.pivotal.workshops.pkskafka.analytics.domain;

import java.util.Arrays;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import io.pivotal.workshops.pkskafka.analytics.AnalyticsBindings;
import io.pivotal.workshops.pkskafka.domain.events.order.LineItem;
import io.pivotal.workshops.pkskafka.domain.events.order.Order;
import io.pivotal.workshops.pkskafka.domain.events.order.State;
import lombok.extern.apachecommons.CommonsLog;

@Component
@CommonsLog
public class OrderEventProcessor {
	
	@StreamListener
	@SendTo (AnalyticsBindings.PRODUCT_COUNT_OUT)
	public KStream<String,Long> process (@Input (AnalyticsBindings.ORDER_IN) KStream<String, Order> orderEventStream)
	{
		KStream<String, Long> outputStream = null;
		try {
	
			//orderEventStream.foreach(
			//		(key,value)->log.info("Messages in Order Event Processor ---> " +key + " = " + value.getOrderId()));
			
			
			outputStream = orderEventStream
					.filter((key,value) -> value.getState().equals(State.validated))
					.flatMapValues(value -> value.getLineItems())
					.map((key,value)-> new KeyValue<>(value.getSku().toString(), "0"))
					.groupByKey()
					.count(Materialized.as(AnalyticsBindings.PRODUCT_COUNT_MV))
					.toStream();
			
			outputStream.foreach((key,value)->log.info("Key and Value at analytics " + key+ "-" + value));

		} catch (Exception ex) 
		{
			log.error(ex.getMessage());
		}
		return outputStream;
	
	}
}
