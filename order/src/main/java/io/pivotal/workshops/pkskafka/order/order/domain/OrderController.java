package io.pivotal.workshops.pkskafka.order.order.domain;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.schema.client.ConfluentSchemaRegistryClient;
import org.springframework.cloud.stream.schema.client.SchemaRegistryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.pivotal.workshops.pkskafka.domain.events.order.Order;
import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
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
	  public String saveOrder(@RequestBody OrderDTO order) {
		
		return service.createOrder(order);
		  
	  }	
	  
}
