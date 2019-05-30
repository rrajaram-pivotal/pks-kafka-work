package io.pivotal.workshops.pkskafka.order.order.domain;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import io.pivotal.workshops.pkskafka.domain.events.order.Order;
import io.pivotal.workshops.pkskafka.order.ResourceBinding;
import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
//@Component
public class OrderStoreProcessor {
	
/*	@StreamListener
	public void process (@Input (ResourceBinding.ORDER_IN) KTable<String, Order> orderEventStream)
	{
		try {
	
			String storename = orderEventStream.queryableStoreName();
			log.info("--------------------------------------------------" + storename);

		} catch (Exception ex) 
		{
			log.error(ex.getMessage());
		}
		
	}
*/
}
