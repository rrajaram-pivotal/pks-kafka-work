package io.pivotal.workshops.pkskafka.analytics.domain;

import lombok.extern.apachecommons.CommonsLog;
import org.apache.kafka.streams.kstream.KTable;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import io.pivotal.workshops.pkskafka.analytics.AnalyticsBindings;

@Component
@CommonsLog
public class ProductCountSink {

	@StreamListener 
	public void productCount (@Input (AnalyticsBindings.PRODUCT_COUNT_IN) KTable<String, Long> productCounts)
	{
		
		try {
			productCounts.toStream()
				.foreach((key,value)->log.info(key+"="+value));
		} catch (Exception ex)
		{
			log.error("Exception at ProductCount Sink" + ex.getMessage());
		}
	}
	
}
