package io.pivotal.workshops.pkskafka.inventory.domain;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.ValueJoiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import io.pivotal.workshops.pkskafka.domain.events.inventory.Inventory;
import io.pivotal.workshops.pkskafka.domain.events.order.LineItem;
import io.pivotal.workshops.pkskafka.domain.events.order.LineItemState;
import io.pivotal.workshops.pkskafka.domain.events.order.Order;
import io.pivotal.workshops.pkskafka.domain.events.order.State;
import io.pivotal.workshops.pkskafka.domain.events.ordervalidation.OrderValidation;
import io.pivotal.workshops.pkskafka.inventory.InventoryBindings;
import io.pivotal.workshops.pkskafka.inventory.domain.OrderValidationProcessor;
import lombok.extern.apachecommons.CommonsLog;

//@CommonsLog
//@Component
public class UpdateInventoryProcessor {
	
	/*@Autowired
	InventoryService inventoryService;
	
	@StreamListener
	//@SendTo (InventoryBindings.INVENTORY_STREAM_OUT)
	public void updateInventory (
			@Input (InventoryBindings.UPDATE_INV_ORDER_STREAM_IN) KStream<String, Order> validatedOrderStream) //Inventory Stream Materialized as GlobalKTable
	{
		validatedOrderStream
			.filter((key,value) -> value.getState().equals(State.validated))
			.flatMapValues(value -> value.getLineItems())
			.selectKey((orderId, lineItem) -> lineItem.getSku().toString())
			.foreach((key,value) -> inventoryService.updateInventory(key));
			.join(inventory,
				new KeyValueMapper<String, LineItem, String>() {

					@Override
					public String apply(String key, LineItem value) {
						log.info("Sku Key In Update Inventory" + key.toString());
						return key;
					}	
				}, 	
				new ValueJoiner<LineItem, Inventory, Inventory>() {
					@Override
					public Inventory apply(LineItem value1, Inventory value2) {
						log.info("Value From Line Item " + value1.getSku() + " - " + value1.getState() + " Value 2 " + value2.getSku() +  " Inventory " + value2.getQuantity() );
						if (value1.getState().equals(LineItemState.validated))
						{
							value2.setQuantity(value2.getQuantity()-1);
							log.info("Value From Line Item " + value2.getSku() + " Value 2 " + value2.getSku() + " Updated Inventory " + value2.getQuantity() );
						}
						
						return value2;
				}
	//	});

		 
	}*/

}
