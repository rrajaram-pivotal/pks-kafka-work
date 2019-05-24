package io.pivotal.workshops.pkskafka.inventory.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

import io.pivotal.workshops.pkskafka.inventory.InventoryBindings;
import io.pivotal.workshops.pkskafka.domain.events.inventory.Inventory;
import lombok.extern.apachecommons.CommonsLog;

@Component
@CommonsLog
public class InventoryEventSource implements ApplicationRunner {
	
	
	@Autowired
	InventoryService inventoryService;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		
		// Random list of products and quantities
		List<String> sku = Arrays.asList("NIKEGREENCL12345", "NIKEBLUECL12345","NIKEBLACKCL12345","NIKEREDCL12345",
				"BRKSREDSH5567801","BRKSBLUESH5567801","BRKSBLACKSH5567801","BRKSSPECSH5567801",
				"ADIDREDBALL6756","ADIDBLUEBALL6756","ADIDYELLOWBALL6756","ADIDGREENBALL6756",
				"NWBBLUEAIRMAX10823","NWBBLUEAIRMAX10823","NWBREDAIRMAX10823","NWBGRAYAIRMAX10823");
		List<Integer> inventory = Arrays.asList(10,20,15,0,30,50);

		
		
		Runnable updateInventory = () -> {
			try {
				//this.inventoryOut.send(message);
				inventoryService.replenishInventory();
				log.info("Ineventory replenished ");
				
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				log.error("Exception in Update Inventory  " + ex.toString());
			}
			
			
		};
		
		Runnable createInventory = () -> {
			try {
				inventoryService.createInventory(sku, inventory);
				log.info("Inventory Created On Start Up" );
				
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				log.error("Exception at Inventory Source  " + ex.toString());
			}
			
			
		};
				
		
		//  Executor to run the runnable task of generating inventory updates every 30 minutes
		Executors.newScheduledThreadPool(1).execute(createInventory);
		Executors.newScheduledThreadPool(1).scheduleAtFixedRate(updateInventory, 1, 15, TimeUnit.MINUTES);
		
		
	}


}
