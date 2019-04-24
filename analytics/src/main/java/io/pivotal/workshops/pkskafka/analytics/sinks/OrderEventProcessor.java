package io.pivotal.workshops.pkskafka.analytics.sinks;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;



import io.pivotal.workshops.pkskafka.analytics.binding.AnalyticsBindings;
import io.pivotal.workshops.pkskafka.analytics.events.OrderEvent;
import lombok.extern.apachecommons.CommonsLog;

@Component
@CommonsLog
public class OrderEventProcessor {
	
	@StreamListener
	@SendTo (AnalyticsBindings.PRODUCT_COUNT_OUT)
	public KStream<String, Long> process (@Input (AnalyticsBindings.ORDER_IN) KStream<String, OrderEvent> orderEventStream)
	{
		try {
	
			//orderEventStream.foreach(
			//		(key,value)->log.info("Messages in Order Event Processor ---> " +key + " = " + value.getOrderId()));
			return orderEventStream
					.map((key,value)-> new KeyValue<>(value.getProductId(), "0"))
					.groupByKey()
					.count(Materialized.as(AnalyticsBindings.PRODUCT_COUNT_MV))
					.toStream();
		} catch (Exception ex) 
		{
			log.error(ex.getMessage());
		}
		
		return null;
	}
}
