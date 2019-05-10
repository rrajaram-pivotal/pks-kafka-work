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
import io.pivotal.workshops.pkskafka.ResourceBinding;
import io.pivotal.workshops.pkskafka.inventory.domain.events.Inventory;
import io.pivotal.workshops.pkskafka.order.domain.events.LineItem;
import io.pivotal.workshops.pkskafka.order.domain.events.LineItemState;
import io.pivotal.workshops.pkskafka.order.domain.events.Order;
import io.pivotal.workshops.pkskafka.order.domain.events.State;
import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
@Component
public class InventoryProcessor {
	
	@Autowired
	private InteractiveQueryService interactiveQueryService;
	
	@Autowired
	private InventoryService inventoryService;
	

	//private MessageChannel inventoryOut;

	
	@StreamListener
	@SendTo (ResourceBinding.ORDER_STREAM_OUT)
	public KStream<String, Order> validateOrder (@Input (ResourceBinding.ORDER_STREAM_IN) KStream<String, Order> orderEventStream,
			@Input (ResourceBinding.INVENTORY_IN) GlobalKTable<String, Integer> inventoryStream)
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
										
										log.info("Updating Inventory after validating Order " + lineItem.getSku());
										Inventory inventory = inventoryService.fetchInventoryById(lineItem.getSku().toString());
										if (inventory.getQuantity() > 0) {
											lineItem.setState(LineItemState.validated);
											inventoryService.updateInventory(lineItem.getSku().toString());		
										}
										else {
											lineItem.setState(LineItemState.insufficient_inventory);
											allItemsValidated=false;
										}
																				
									}
									if (allItemsValidated == true)
										value.setState(State.validated);
									else
										value.setState(State.insufficient_inventory);
									return value;
									});			
						
			/*orderEventStream.filter((key,value) -> (value.getState().equals(State.validated) ) )
					.flatMapValues(new ValueMapper<Order, Iterable<LineItem>>() {
						@Override
						public Iterable<LineItem> apply(Order value) {
						 return value.getLineItems();
						}
					}).foreach((key,value) -> {
						log.info("Updating Inventory after validating Order " + value.getSku());
						inventoryService.updateInventory(value.getSku().toString());
					
						});*/
			} catch (Exception ex) 
			{
				log.error(ex.getMessage());
			}
			return outputStream;
	}
}
