package io.pivotal.workshops.pkskafka.inventory.domain;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import io.pivotal.workshops.pkskafka.inventory.InventoryBindings;
import io.pivotal.workshops.pkskafka.domain.events.inventory.Inventory;
import io.pivotal.workshops.pkskafka.domain.events.order.Order;
import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
@Service
public class InventoryService {
	
		@Autowired
		private InteractiveQueryService interactiveQueryService;
	
	 	private final MessageChannel inventoryOut;
	    
	    /**
		 * Constructor with bindings for consuming the output channel to the "inventory-topic" topic
		 * @param binding
		 */
		public InventoryService(InventoryBindings binding) {
			this.inventoryOut = binding.inventoryOut();
		}

		/**
		 * Deducts Inventory once order is validated
		 * @param sku
		 */
	  	public void updateInventory(String sku)
	  	{
	  		
	  		try {
		  		Inventory inventory = fetchInventoryById(sku);
		  		log.info("Inventory Record found by Sku " + inventory.toString());
		  		if (inventory != null)
		  		{
		  			inventory.setQuantity(inventory.getQuantity()-1);
					Message<Inventory> message = MessageBuilder.withPayload(inventory).setHeader(
							KafkaHeaders.MESSAGE_KEY, inventory.getSku().toString()).build();
					
					inventoryOut.send(message);
					log.info("Inventory Update Message Sent " + message);
		  		}
	  		} catch (Exception ex) {
	  			ex.printStackTrace();
	  			log.error(ex.getMessage());
	  		}
	  	}		
		
		
		
		
		/**
		 * Queries the inventory store to get the record for the given sku
		 * @param sku Sku Identifying the inventory record
		 * @return
		 */
	  	public Inventory fetchInventoryById(String sku) 
	  	{
	  		Inventory inventory = null;
	  		
	  		try {
		  		final ReadOnlyKeyValueStore<String, Inventory> inventoryStore =
						interactiveQueryService.getQueryableStore(InventoryBindings.INVENTORY_STORE, 
								QueryableStoreTypes.<String, Inventory>keyValueStore());
				log.info("Fetching Inventory For SKU" + sku);
				
				
				inventory = inventoryStore.get(sku);
				log.info("Inventory Available For SKU " + sku + " is " + inventory.getQuantity());
				
	  		} catch (Exception ex) {
	  			ex.printStackTrace();
	  			log.error(ex.getMessage());
	  		}
	  		return inventory;
	  	}		
	  	
	  	
	  	/**
		 * Queries the inventory store to get the record for the given sku
		 * @param sku Sku Identifying the inventory record
		 * @return
		 */
	  	public void replenishInventory() throws Exception
	  	{
	  		List<Integer> randomQuantity = Arrays.asList(10,20,15,0,30,50);
	  		Integer quantity = 0;
	  		
	  		try {
		  		final ReadOnlyKeyValueStore<String, Inventory> inventoryStore =
						interactiveQueryService.getQueryableStore(InventoryBindings.INVENTORY_STORE, 
								QueryableStoreTypes.<String, Inventory>keyValueStore());
		  		KeyValueIterator<String,Inventory> items = inventoryStore.all();
		  		KeyValue<String, Inventory> keyValue = null;
		  		Inventory inventory = null;
		  		Message<Inventory> message =null;
				while (items.hasNext())
				{
					quantity = randomQuantity.get (new Random().nextInt(randomQuantity.size()));
					keyValue = items.next();
					inventory = keyValue.value;
					if (inventory.getQuantity() == 0)
					{
						inventory.setQuantity(quantity);
						message = MessageBuilder.withPayload(inventory).setHeader(
								KafkaHeaders.MESSAGE_KEY, inventory.getSku().toString()).build();						
						inventoryOut.send(message);
						log.info("Inventory Replenished " + message);		
					}
					
				}
				
	  		} catch (Exception ex) {
	  			ex.printStackTrace();
	  			throw ex;
	  		}
	  	
	  	}		
		
	  	/**
		 * Queries the inventory store to get the record for the given sku
		 * @param sku Sku Identifying the inventory record
		 * @return
		 */
	  	public void createInventory(List<String> items, List<Integer> randomQuantity) throws Exception
	  	{
	  		
	  		Integer quantity = 0;
	  		String sku = null;
	  		
	  		try {
		  		final ReadOnlyKeyValueStore<String, Inventory> inventoryStore =
						interactiveQueryService.getQueryableStore(InventoryBindings.INVENTORY_STORE, 
								QueryableStoreTypes.<String, Inventory>keyValueStore());
		  		KeyValueIterator<String,Inventory> values = inventoryStore.all();
		  		Inventory inventory = null;
		  		/*if (values.hasNext())
		  		{
		  			log.info("Inventory Data Present");
		  			return;
		  		}*/
		  		Iterator<String> iterator = items.iterator();
	  			while (iterator.hasNext())
	  			{
	  				sku = iterator.next();
	  				quantity = randomQuantity.get (new Random().nextInt(randomQuantity.size()));
			  		inventory = new Inventory();
			  		inventory.setQuantity(quantity);
			  		inventory.setSku(sku);
			  		Message<Inventory> message = MessageBuilder.withPayload(inventory).setHeader(
							KafkaHeaders.MESSAGE_KEY, inventory.getSku().toString()).build();						
					inventoryOut.send(message);
					log.info("Inventory Added For SKU " + sku +"-" +quantity);		
	  					  				
	  			}
	  			log.info("Inventory Created");
	  		} catch (Exception ex) {
	  			ex.printStackTrace();
	  			throw ex;
	  		}
	  	
	  	}		
		


}
