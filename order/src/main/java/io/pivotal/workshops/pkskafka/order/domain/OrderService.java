package io.pivotal.workshops.pkskafka.order.domain;

import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import io.pivotal.workshops.pkskafka.order.domain.events.LineItem;
import io.pivotal.workshops.pkskafka.order.domain.events.LineItemState;
import io.pivotal.workshops.pkskafka.order.domain.events.Order;
import io.pivotal.workshops.pkskafka.order.domain.events.State;
import lombok.extern.apachecommons.CommonsLog;

@Service
@CommonsLog
public class OrderService {
	
	

    private final MessageChannel orderOut;
    
    /**
	 * Constructor with bindings for consuming the output channel to the "orders" topic
	 * @param binding
	 */
	public OrderService(OrderBinding binding) {
		this.orderOut = binding.orderOut();
	}

	
	public String createOrder(OrderDTO orderDTO) 
	{
		log.info("Creating Order ");
		String response = "";
		OrderMapper orderMapper = new OrderMapper();
		try {
			log.info("Mapping Order Input to Order Event " + orderDTO.toString());
			Order order = orderMapper.convertOrderDTOToEvent(orderDTO);
			log.info("Order Event Created " + order.toString());
			if (order != null)
			{
				order.setOrderID(UUID.randomUUID().toString());
				order.setState(State.placed);
				LineItem lineItem;
				for (Iterator<LineItem> i = order.getLineItems().iterator(); i.hasNext(); ) 
				{
					lineItem = i.next();
					lineItem.setState(LineItemState.in_process);
					
				}
				
			}
			log.info("Order Event State set " + order.toString());
			log.info("Order Out Channel " + orderOut);
			orderOut.send(MessageBuilder.withPayload(order).setHeader("kafka_messageKey", order.getOrderID()).build());
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			log.error("Exception while creating order event " +ex.getMessage() );
			response = ex.getMessage();
		}
		
		return response;
		
		
	}
	
}
