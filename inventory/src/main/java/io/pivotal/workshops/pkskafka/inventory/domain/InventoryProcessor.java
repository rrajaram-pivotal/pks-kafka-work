package io.pivotal.workshops.pkskafka.inventory.domain;

import java.util.Arrays;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serdes.StringSerde;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.Joined;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.kstream.ValueMapper;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import io.pivotal.workshops.pkskafka.inventory.domain.events.Inventory;
import io.pivotal.workshops.pkskafka.inventory.domain.events.LineItem;
import io.pivotal.workshops.pkskafka.inventory.domain.events.Order;
import io.pivotal.workshops.pkskafka.inventory.domain.events.State;
import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
@Component
public class InventoryProcessor {
	
	@StreamListener
	@SendTo ()
	public KStream<String,Order> process (@Input (InventoryBindings.ORDER_IN) KStream<String, Order> orderEventStream,
			@Input (InventoryBindings.INVENTORY_IN) KTable<String, Inventory> inventoryStream)
	{
		try {
		
			return orderEventStream.foreach((key,value) -> value.setState(State.validated));
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
		
	}
	

}
