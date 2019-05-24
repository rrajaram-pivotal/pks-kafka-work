package io.pivotal.workshops.pkskafka.queryorder.domain;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.pivotal.workshops.pkskafka.queryorder.domain.OrderDTO;
import io.pivotal.workshops.pkskafka.queryorder.domain.QueryOrderService;
import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
@RestController
@RequestMapping("/v1/queryorder")
public class QueryOrderController {
	
	  @Autowired
	  QueryOrderService service;
	  
	  /**
	   * Queries the local KTable for order details based on an id
	   * @param order the order to add
	   * @param timeout the max time to wait for the response from Kafka before timing out the POST
	   */
	  @GetMapping("/{id}")
	  public OrderDTO getOrder(@PathVariable("id") String orderID) {
		  log.info("In Find Order GET Method" );
		 
		  return service.findOrder(orderID);
		  
	  }	 
	  
	  /**
	   * Queries the GlobalKTable for order details based on an id
	   * @param order the order to add
	   * @param timeout the max time to wait for the response from Kafka before timing out the POST
	   */
	  @GetMapping("/")
	  public List<OrderDTO> getAll() {
		  log.info("In Find All Orders GET Method" );
		 
		  return service.fetchAllOrders();
		  
	  }	
	  

}
