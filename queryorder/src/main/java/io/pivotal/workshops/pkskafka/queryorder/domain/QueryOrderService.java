package io.pivotal.workshops.pkskafka.queryorder.domain;

import java.util.ArrayList;
import java.util.List;

import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.HostInfo;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.stereotype.Service;


import io.pivotal.workshops.pkskafka.domain.events.order.Order;
import io.pivotal.workshops.pkskafka.queryorder.*;
import io.pivotal.workshops.pkskafka.queryorder.ResourceBindings;
import lombok.extern.apachecommons.CommonsLog;

@Service
@CommonsLog
public class QueryOrderService {
	
	@Autowired
	private InteractiveQueryService interactiveQueryService;

	  public OrderDTO findOrder(String orderID) {
		    log.info("Running GET on for single instance scenario");
			QueryOrderMapper orderMapper = new QueryOrderMapper();
		    Order order = null;
		    OrderDTO orderDTO = null;
		    try {
		    	
				 log.info("Getting host info for ");
				 HostInfo hostInfo = interactiveQueryService.getHostInfo(ResourceBindings.ORDER_STORE,
						orderID, new StringSerializer());
				log.info("Orders fetched from the same host: " + hostInfo);
				order = fetchOrderByID(orderID);
				orderDTO = orderMapper.convertOrderEventToDTO(order);
				log.info("Order details " +  order.toString());
		    	
		    }catch (Exception ex)
		    {
		    	ex.printStackTrace();
		    	log.error("Exception getting order details" + ex.getMessage());
		    }
		    return orderDTO;
		  }

	    /**
	     * Retrieves all orders in the system
	     * @return
	     */
	  	public List<OrderDTO> fetchAllOrders()
	  	{
	  		KeyValueIterator<String, Order> orderListIterator = null;
	  		
	  		try {
	  		final ReadOnlyKeyValueStore<String, Order> orderStore =
					interactiveQueryService.getQueryableStore(ResourceBindings.ORDER_STORE, 
							QueryableStoreTypes.<String, Order>keyValueStore());
				orderListIterator = orderStore.all();
	  		} catch (Exception ex) {
	  			ex.printStackTrace();
	  		}
	  		KeyValue<String, Order> order = null;
	  		QueryOrderMapper mapper = new QueryOrderMapper();
	  		OrderDTO orderDTO = null;
	  		List<OrderDTO> orderList = new ArrayList<OrderDTO>();
	  		while (orderListIterator.hasNext())
	  		{
	  			order = orderListIterator.next();
	  			orderDTO = mapper.convertOrderEventToDTO(order.value);
	  			orderList.add(orderDTO);
	  			
	  		}
	  		return orderList;
	  	}
	  
	  
	  	private Order fetchOrderByID(String orderID)
	  	{
	  		Order order = null;
	  		
	  		try {
	  		final ReadOnlyKeyValueStore<String, Order> orderStore =
					interactiveQueryService.getQueryableStore(ResourceBindings.ORDER_STORE, 
							QueryableStoreTypes.<String, Order>keyValueStore());
	  		System.out.println("Current Hostb Infor --- > " + interactiveQueryService.getCurrentHostInfo());
			System.out.println("Find Order for ID --> " + orderID + "in Order Store " + orderStore);
			System.out.println(orderStore.all().toString());
			order = orderStore.get(orderID);
	  		} catch (Exception ex) {
	  			ex.printStackTrace();
	  		}
	  		return order;
	  	}

}
