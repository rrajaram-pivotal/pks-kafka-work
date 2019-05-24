package io.pivotal.workshops.pkskafka.queryorder.domain;

import org.apache.kafka.streams.kstream.KTable;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import io.pivotal.workshops.pkskafka.domain.events.order.Order;
import io.pivotal.workshops.pkskafka.queryorder.ResourceBindings;
import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
@Component
public class QueryOrderStreamProcessor {
	

	@StreamListener
	public void process (@Input (ResourceBindings.ORDER_STREAM_IN) KTable<String, Order> orderEventStream)
	{
		try {
	
			String storename = orderEventStream.queryableStoreName();
			log.info("--------------------------------------------------" + storename);

		} catch (Exception ex) 
		{
			log.error(ex.getMessage());
		}
		
	}

}
