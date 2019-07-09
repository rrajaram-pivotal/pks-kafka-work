package io.pivotal.workshops.pkskafka.order.order.domain;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import io.pivotal.workshops.pkskafka.domain.events.inventory.Inventory;
import io.pivotal.workshops.pkskafka.domain.events.order.Customer;
import io.pivotal.workshops.pkskafka.domain.events.order.CustomerAddr;
import io.pivotal.workshops.pkskafka.domain.events.order.LineItem;
import io.pivotal.workshops.pkskafka.order.ResourceBinding;
import lombok.extern.apachecommons.CommonsLog;

@Component
@CommonsLog
public class OrderEventSource implements ApplicationRunner {

	
	@Autowired
	OrderService orderService;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		
		// Random List Of Customer Addresses
		List<String> addressLine = Arrays.asList("Balmoral Ct","Patricia Ln", "Cheney Rd", "Longwood Dr", "TallTrees Dr","Elgin Circle","Aberdeen Ct","Springhill Ext","Court Street","Windhaul Dr", "Champion Dr");
		List<Integer> houseNumber = Arrays.asList(820,908,654,34,245,65,554,821,823,843,909,910,911,912);
	
		List<String> cities = Arrays.asList("Moon","Pitsburgh","Philadelphia");
		List<String> zipcodes = Arrays.asList("15108","15212","19093");
		List<String> state = Arrays.asList("PA");
		
		
		List<String> customerFirstNames = Arrays.asList("Matt","Brad", "Linsey", "Paulina", "Laura","Ryan","Tiffany","Clint","Jake","Harrington", "Duke");
		List<String> customerLastNames = Arrays.asList("Smith","Bayer", "Egler", "Keys", "Lesher","Kennedy","Koensceni","Peter","Jacoby","Harrington", "Duke");
		List <Boolean> automatedEmails =  Arrays.asList(Boolean.TRUE, Boolean.FALSE);
		

		Map<String, String> skuUPCMap = new HashMap<String, String>();
		skuUPCMap.put("NIKEGREENCL12345", "NIKECL12345");
		skuUPCMap.put("NIKEBLUECL12345", "NIKECL12345");
		skuUPCMap.put("NIKEBLACKCL12345", "NIKECL12345");
		skuUPCMap.put("NIKEREDCL12345", "NIKECL12345");
		skuUPCMap.put("BRKSREDSH5567801", "BRKSSH5567801");
		skuUPCMap.put("BRKSBLUESH5567801", "BRKSSH5567801");
		skuUPCMap.put("BRKSBLACKSH5567801", "BRKSSH5567801");
		skuUPCMap.put("BRKSSPECSH5567801", "BRKSSH5567801");
		skuUPCMap.put("ADIDREDBALL6756", "ADIDBALL6756");
		skuUPCMap.put("ADIDBLUEBALL6756", "ADIDBALL6756");
		skuUPCMap.put("ADIDYELLOWBALL6756", "ADIDBALL6756");
		skuUPCMap.put("ADIDGREENBALL6756", "ADIDBALL6756");
		skuUPCMap.put("NWBBLUEAIRMAX10823", "NWBAIRMAX10823");
		skuUPCMap.put("NWBBLUEAIRMAX10823", "NWBAIRMAX10823");
		skuUPCMap.put("NWBREDAIRMAX10823", "NWBAIRMAX10823");
		skuUPCMap.put("NWBGRAYAIRMAX10823", "NWBAIRMAX10823");
		
		List<String> skus = Arrays.asList("NIKEGREENCL12345", "ADIDREDBALL6756","BRKSREDSH5567801",
				"ADIDBLUEBALL6756","NWBREDAIRMAX10823","NIKEBLUECL12345","NIKEBLACKCL12345","BRKSBLUESH5567801","BRKSBLACKSH5567801","ADIDBLUEBALL6756",
				"NWBBLUEAIRMAX10823","NWBBLUEAIRMAX10823"
				);
		List<Float> purchasePrices = Arrays.asList(85.99f,76.99f,64.99f,54.00f,81.99f,90.00f,102.99f,120.90f);

		
		RestTemplate restTemplate = new RestTemplate();
		Runnable generateOrder = () -> {
			try {
				int cityIndex = new Random().nextInt(cities.size());
				CustomerAddressDTO address = new CustomerAddressDTO();
				address.setAddress(addressLine.get (new Random().nextInt(addressLine.size())));
				address.setCity(cities.get (cityIndex));
				address.setZipcode(zipcodes.get(cityIndex));
				address.setState("PA");
						
				String lastName = customerLastNames.get (new Random().nextInt(customerLastNames.size()));
				String firstName = customerFirstNames.get (new Random().nextInt(customerFirstNames.size()));
				CustomerDTO customer = new CustomerDTO();
				customer.setLastName(lastName);
				customer.setFirstName(firstName);
				customer.setAutomatedEmail(automatedEmails.get (new Random().nextInt(automatedEmails.size())));
				customer.setCustomerEmails(Arrays.asList("lastName" + "." + "firstName" + "@gmail.com"));
				customer.setCustomerAddress(Arrays.asList(address));
						
				
				String sku = skus.get (new Random().nextInt(skus.size()));
				LineItemDTO lineItem = new LineItemDTO();
				lineItem.setSku(sku);
				lineItem.setUpc(skuUPCMap.get(sku));
				lineItem.setPurchasePrice(purchasePrices.get (new Random().nextInt(purchasePrices.size())));
				lineItem.setState("Purchased");
				lineItem.setLineNumber(1);
				lineItem.setState("placed");
				
				String currentDate=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Calendar.getInstance().getTime());
				OrderDTO order = new OrderDTO();
				order.setCustomer(customer);
				List<LineItemDTO> items = new ArrayList<LineItemDTO>();
				items.add(lineItem);
						
				order.setLineItems(items);
				order.setState("placed");
			
				order.setTimePlaced(currentDate);
				order.setLastUpdated(currentDate);
				
				
				
				String result = restTemplate.postForObject("http://localhost:8087/v1/order", order, String.class);
				
				
				log.info("Order Generated For Order ID " + result);
				
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				log.error("Exception in order generation " + ex.toString());
			}
			
			
		};
		
		/*Runnable createInventory = () -> {
			try {
				//inventoryService.createInventory(sku, inventory);
				log.info("Inventory Created On Start Up");
				
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				log.error("Exception at Inventory Source  " + ex.toString());
			}
			
			
		};*/
				
		
		//  Executor to run the runnable task of generating inventory updates every 30 minutes
		//Executors.newScheduledThreadPool(1).execute(createInventory);
		Executors.newScheduledThreadPool(1).scheduleAtFixedRate(generateOrder, 1, 1, TimeUnit.MINUTES);
		
		
	}


}
