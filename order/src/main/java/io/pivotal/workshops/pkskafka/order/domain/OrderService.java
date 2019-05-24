package io.pivotal.workshops.pkskafka.order.domain;

import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.HostInfo;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import io.pivotal.workshops.pkskafka.ResourceBinding;
import io.pivotal.workshops.pkskafka.domain.events.order.*;
import lombok.extern.apachecommons.CommonsLog;

@Service
@CommonsLog
public class OrderService {
	
	@Autowired
	private InteractiveQueryService interactiveQueryService;

    private final MessageChannel orderOut;
    
    /**
	 * Constructor with bindings for consuming the output channel to the "orders" topic
	 * @param binding
	 */
	public OrderService(ResourceBinding binding) {
		this.orderOut = binding.orderOut();
	}

	
	public String createOrder(OrderDTO orderDTO) 
	{
		log.info("Creating Order ");
		String response = "";
		OrderMapper orderMapper = new OrderMapper();
		try {
			log.info("Mapping Order Input to Order Event " + orderDTO.toString());
			Order order = null;
			
			
			
			if (orderDTO != null)
			{
				if (orderDTO.getOrderID() == null) {
					order = orderMapper.convertOrderDTOToEvent(orderDTO);
					order.setOrderID(UUID.randomUUID().toString());
					order.setState(State.placed);
					log.info("Order Mapping To Event Done ---" + order.toString());
					LineItem lineItem;
					for (Iterator<LineItem> i = order.getLineItems().iterator(); i.hasNext(); ) 
					{
						lineItem = i.next();
						lineItem.setState(LineItemState.in_process);
						lineItem.setEstimatedDeliveryDate("");
						
					}					
				}
				else 
				{
					order = fetchOrderByID(orderDTO.getOrderID());
					log.info("Order Retrieved By ID -----> " + order.toString());
					order = orderMapper.convertOrderDTOToEvent(orderDTO, order);
					log.info("Order After Mapping with Mapper -----> " + order.toString());
				}
				
				log.info("Order Event Created " + order.toString());
			}
			log.info("Order Event State set " + order.toString());
			log.info("Order Out Channel " + orderOut);
			orderOut.send(MessageBuilder.withPayload(order).setHeader("kafka_messageKey", order.getOrderID()).build());
			response = order.getOrderID().toString();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			log.error("Exception while creating order event " +ex.getMessage() );
			response = ex.getMessage();
		}
		return response;
	}
	
	
		/**
		 * Fetches Order details for the given order id. This is used to generate
		 * an update event.
		 * @param orderID
		 * @return
		 */
	  	private Order fetchOrderByID(String orderID)
	  	{
	  		Order order = null;
	  		
	  		try {
	  		final ReadOnlyKeyValueStore<String, Order> orderStore =
					interactiveQueryService.getQueryableStore(ResourceBinding.ORDER_STORE, 
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
