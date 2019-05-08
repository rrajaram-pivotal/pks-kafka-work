package io.pivotal.workshops.pkskafka.order.domain;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.schema.client.ConfluentSchemaRegistryClient;
import org.springframework.cloud.stream.schema.client.SchemaRegistryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.pivotal.workshops.pkskafka.order.domain.events.Order;

@RestController
@RequestMapping("/v1/order")
public class OrderController {
	
	  @Autowired
	  OrderService service;

	  /**
	   * Persist an Order to Kafka. Returns once the order is successfully written to R nodes where
	   * R is the replication factor configured in Kafka.
	   *
	   * @param order the order to add
	   * @param timeout the max time to wait for the response from Kafka before timing out the POST
	   */
	  @PostMapping (consumes = "application/json")
	  public String submitOrder(@RequestBody OrderDTO order) {
		  //Test Code
			order = new OrderDTO();
			CustomerDTO customerDTO = new CustomerDTO();
			CustomerAddressDTO customerAddressDTO = new CustomerAddressDTO();
			LineItemDTO lineItemDTO = new LineItemDTO();
			lineItemDTO.setSku("NIKCLE78888001");
			lineItemDTO.setUpc("0123456789012");
			lineItemDTO.setEstimatedUnitTax(7.5f);
			lineItemDTO.setLineNumber(1);
			lineItemDTO.setOriginialPrice(54.99f);
			lineItemDTO.setPurchasePrice(44.00f);
			lineItemDTO.setDiscount(20);
			lineItemDTO.setEstimatedDeliveryDate("");
			
			
			customerAddressDTO.setAddress("820 Balmoral Ct");
			customerAddressDTO.setCity("Glen Mills");
			customerAddressDTO.setState("PA");
			customerAddressDTO.setZipcode("12342");
			customerDTO.setFirstName("John");
			customerDTO.setLastName("Smith");
			customerDTO.setCustomerAddress(Arrays.asList(customerAddressDTO));
			customerDTO.setCustomerEmails(Arrays.asList("john.smith@google.com"));
			customerDTO.setAutomatedEmail(true);
			
			order.setLastUpdated("05/02/2019");
			order.setTimePlaced("05/02/2019");
			order.setLineItems(Arrays.asList(lineItemDTO));
			order.setCustomer(customerDTO);
		  
		  
		  
		  return service.createOrder(order);
		  
	  }	
	
	
/*	@PostMapping("/orderstream")
	//@Consumes(MediaType.APPLICATION_JSON)
	public void streamOrders(@RequestBody OrderDTO orderbean) {
		
		System.out.println("OrderID:" + orderbean.getId());
		System.out.println("OrderDTO:" + orderbean.toString());
		
		Order order = Order.newBuilder()
				.setId(orderbean.getId())
				.setCustomerId(orderbean.getCustomerId())
				//.setState("CREATED")
				.setProduct(orderbean.getProduct())
				.setQuantity(orderbean.getQuantity())
				.setPrice(orderbean.getPrice())
				.build();
		
		Message<Order> message = MessageBuilder.withPayload(order)
				.setHeader("kafka_messageKey", order.getId())
				//.setHeader("contentType", "application/*+avro")
				.build();
		//this.orderOut.send(message);
		source.output().send(org.springframework.messaging.support.MessageBuilder.withPayload(order)
				.setHeader("kafka_messageKey", order.getId())
				.build());
		//source.output().send(message);


	}*/
	
	
}
