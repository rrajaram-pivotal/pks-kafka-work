package io.pivotal.workshops.pkskafka.inventory.domain;



import java.util.Arrays;
import java.util.Iterator;

import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.kstream.ValueJoiner;
import org.apache.kafka.streams.kstream.ValueMapper;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import io.pivotal.workshops.pkskafka.inventory.InventoryBindings;
import io.pivotal.workshops.pkskafka.domain.events.inventory.Inventory;
import io.pivotal.workshops.pkskafka.domain.events.order.LineItem;
import io.pivotal.workshops.pkskafka.domain.events.order.LineItemState;
import io.pivotal.workshops.pkskafka.domain.events.order.Order;
import io.pivotal.workshops.pkskafka.domain.events.order.State;
import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
@Component
public class InventoryProcessor {
	
	@Autowired
	private InteractiveQueryService interactiveQueryService;
	
	@Autowired
	private InventoryService inventoryService;
	

	private MessageChannel inventoryOut;

	
	@StreamListener
	@SendTo (InventoryBindings.ORDER_STREAM_OUT)
	public KStream<String, Order> validateOrder (@Input (InventoryBindings.ORDER_STREAM_IN) KStream<String, Order> orderEventStream,
			@Input (InventoryBindings.INVENTORY_IN) GlobalKTable<String, Integer> inventoryStream)
	{
		KStream<String,Order> outputStream = null;
		
		try {
			
			outputStream = orderEventStream.filter((key,value) -> value.getState().equals(State.placed))
								.mapValues(value -> {
									boolean allItemsValidated = true;
									Iterator<LineItem> iterator = value.getLineItems().iterator();
									while (iterator.hasNext())
									{
										LineItem lineItem = iterator.next();
										Inventory inventory = inventoryService.fetchInventoryById(lineItem.getSku().toString());
										
										if (inventory != null && inventory.getQuantity() > 0) {
											lineItem.setState(LineItemState.validated);
											inventoryService.updateInventory(lineItem.getSku().toString());		
										}
										else {
											
											lineItem.setState(LineItemState.insufficient_inventory);
											allItemsValidated=false;
											log.info("Line Item Validation Failed For Sku " + lineItem.getSku() + "" + lineItem.getState());
										}
										log.info("Line Item Status For " + lineItem.getSku() + "" + lineItem.getState());
										
																				
									}
									log.info("All Items Validated For Order " + value.getOrderID() + " Status " + allItemsValidated);
									if (allItemsValidated == true)
										value.setState(State.validated);
									else
										value.setState(State.insufficient_inventory);
									return value;
									});	
			
			outputStream.foreach((key,value)->log.info("Validating Order " + value.getOrderID()));
						
			
			} catch (Exception ex) 
			{
				ex.printStackTrace();
				log.error(ex.getMessage());
			}
			return outputStream;
	}
}
