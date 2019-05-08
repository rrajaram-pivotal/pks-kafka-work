package io.pivotal.workshops.pkskafka.order.domain;


import io.pivotal.workshops.pkskafka.order.domain.events.Order;
import ma.glasnost.orika.BoundMapperFacade;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

public class OrderMapper {

	
		private MapperFactory mapperFactory ;
	 	
	    public OrderMapper() {
	    	mapperFactory = new DefaultMapperFactory.Builder().build();
	    	
	    }
	 
	  
	    public Order convertOrderDTOToEvent(OrderDTO sourceOrder) {
	    	BoundMapperFacade<OrderDTO, Order> 
	         boundMapper = mapperFactory.getMapperFacade(OrderDTO.class, Order.class);
	        return boundMapper.map(sourceOrder);
	    }
	    
	    
	    public OrderDTO convertOrderEventToDTO(Order sourceOrderEvent) {
	    	BoundMapperFacade<OrderDTO, Order> 
	         boundMapper = mapperFactory.getMapperFacade(OrderDTO.class, Order.class);
	        return boundMapper.mapReverse(sourceOrderEvent);
	    }
	
	
}
