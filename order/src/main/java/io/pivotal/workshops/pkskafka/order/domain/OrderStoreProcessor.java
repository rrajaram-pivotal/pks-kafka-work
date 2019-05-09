package io.pivotal.workshops.pkskafka.order.domain;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import io.pivotal.workshops.pkskafka.order.domain.events.Order;
import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
@Component
public class OrderStoreProcessor {
	
	@StreamListener
	public void process (@Input (OrderBinding.ORDER_IN) GlobalKTable<String, Order> orderEventStream)
	{
		try {
	
			String storename = orderEventStream.queryableStoreName();
			log.info("--------------------------------------------------" + storename);
			//foreach(
			//		(key,value)->log.info("Messages in Order Event Processor ---> " +key + " = " + value.getOrderID()));
			/*return orderEventStream
					.map((key,value)-> new KeyValue<>(value.getProductId(), "0"))
					.groupByKey()
					.count(Materialized.as(AnalyticsBindings.PRODUCT_COUNT_MV))
					.toStream();*/
		} catch (Exception ex) 
		{
			log.error(ex.getMessage());
		}
		
	}

}
