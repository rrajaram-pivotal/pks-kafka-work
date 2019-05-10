package io.pivotal.workshops.pkskafka.inventory.domain;



import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.kstream.ValueMapper;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import io.pivotal.workshops.pkskafka.ResourceBinding;
import io.pivotal.workshops.pkskafka.inventory.domain.events.Inventory;
import io.pivotal.workshops.pkskafka.order.domain.events.Order;
import io.pivotal.workshops.pkskafka.order.domain.events.State;
import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
@Component
public class InventoryProcessor {
	
	@StreamListener
	@SendTo (ResourceBinding.ORDER_STREAM_OUT)
	public KStream<String,Order> validateOrder (@Input (ResourceBinding.ORDER_STREAM_IN) KStream<String, Order> orderEventStream)
	{
		KStream<String,Order> outputStream = null;
		
		try {
			outputStream= orderEventStream.filter((key,value) -> value.getState().equals(State.placed))
					.mapValues(value -> {value.setState(State.in_fulfillment); return value;});
					//.foreach((key,value)->log.info("Messages in Order Event Processor ---> " +key + " = " + value.toString()));
			
			//outputStream.foreach((key,value)->log.info("Messages in Order Event Processor ---> " +key + " = " + value.toString()));
			
						//return outputStream;
						
						/*flatMapValues(new ValueMapper<Order, > {
				
				     Iterable<String> apply(Order value) {
				         return Arrays.asList(value.split(" "));
				     }
				 });
		
		//	orderEventStream.transform((key,value) -> value.setState(State.validated));
			/*inventoryStream.toStream().foreach(
					(key,value)->log.info("Messages in Order Event Processor ---> " +key + " = " + value));*/
			
			
			/*KStream<String, LineItem> productsInOrderStream = orderEventStream
					.filter((id, order) -> "placed".equals(order.getState())).
					flatMapValues(value -> Arrays.asList(value.getLineItems()));*/
			
			
		/*	productsInOrderStream.selectKey((orderId, lineItem) -> lineItem.getSku())
				.join(inventoryTable,Joined.with(Serdes.StringSerde, Serdes.StringSerde(), Serdes.StringSerde()))
				.foreach((key,value)->log.info("Messages in Producs Stream ---> " +key.toString() + " = " + value.toString));*/
				
				
			
			/*.map((key,value)-> new KeyValue<>((value.getLineItems()).forEach((key,value) -> log.info("Line Item Products" + ((LineItem)value).ge))), "0"))
			.groupByKey()
			.count(Materialized.as(AnalyticsBindings.PRODUCT_COUNT_MV))
			.toStream();*/
			
			//log.info("--------------------------------------------------" + storename);
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
		return outputStream;
	}
	

}
