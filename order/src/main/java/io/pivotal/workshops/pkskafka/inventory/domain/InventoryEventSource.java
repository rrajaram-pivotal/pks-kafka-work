package io.pivotal.workshops.pkskafka.inventory.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

import io.pivotal.workshops.pkskafka.ResourceBinding;
import io.pivotal.workshops.pkskafka.inventory.domain.events.Inventory;
import lombok.extern.apachecommons.CommonsLog;

@Component
@CommonsLog
public class InventoryEventSource implements ApplicationRunner {
	
	
	private final MessageChannel inventoryOut;
	/**
	 * Constructor with bindings for consuming the output channel to the "orders" topic
	 * @param binding
	 */
	public InventoryEventSource(ResourceBinding binding) {

		this.inventoryOut	 = binding.inventoryOut();
	}
	
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		
		// Random list of products and quantities
		List<String> sku = Arrays.asList("NIKEGREENCL12345", "ADIDREDBALL6756","BRKSSH5567801","ADIDBLUEBALL6756","NEWBALANCESHOE10823","NIKEBLUECL12345");
		List<Integer> inventory = Arrays.asList(100,1000,200,300,400, 50,60,30,23,64,75, 15,0,20);

		
		
		Runnable runnable = () -> {
			// Random SKU for inventory updates
			
			String skuID = sku.get (new Random().nextInt(sku.size()));
			Integer quantity = inventory.get (new Random().nextInt(inventory.size()));
			
			// Create an quantity update and publish it as an event to the "inventory-event" topic. This would come 
			// from an inventory update job. 
			Inventory inventoryEvent = new Inventory(skuID,quantity);
			Message<Inventory> message = MessageBuilder.withPayload(inventoryEvent).setHeader(
					KafkaHeaders.MESSAGE_KEY, inventoryEvent.getSku()).build();
			
			
			try {
				this.inventoryOut.send(message);
				
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				log.error("Exception at Inventory Source  " + ex.toString());
			}
			
			log.info("Inventory Update Sent " + message.toString());
		};
		
		//  Executor to run the runnable task of generating inventory updates every 15 seconds
		Executors.newScheduledThreadPool(1).scheduleAtFixedRate(runnable, 1, 15, TimeUnit.MINUTES);
		
		
	}


}
