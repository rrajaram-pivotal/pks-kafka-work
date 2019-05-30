package io.pivotal.workshops.pkskafka.order.order.domain;


import io.pivotal.workshops.pkskafka.domain.events.order.Customer;
import io.pivotal.workshops.pkskafka.domain.events.order.CustomerAddr;
import io.pivotal.workshops.pkskafka.domain.events.order.LineItem;
import io.pivotal.workshops.pkskafka.domain.events.order.Order;
import ma.glasnost.orika.BoundMapperFacade;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

public class OrderMapper {

	
		private MapperFactory mapperFactory ;
	 	
	    public OrderMapper() {
	    	
	    	mapperFactory = new DefaultMapperFactory.Builder().build();
	    	mapperFactory.classMap(OrderDTO.class, Order.class)
	    	.mapNulls(false).mapNullsInReverse(true)
	    	.byDefault().register();	
	    	
	    	mapperFactory.classMap(CustomerDTO.class, Customer.class)
	    	.mapNulls(false).mapNullsInReverse(true)
	    	.byDefault().register();	
	    	
	    	mapperFactory.classMap(CustomerAddressDTO.class, CustomerAddr.class)
	    	.mapNulls(false).mapNullsInReverse(true)
	    	.byDefault().register();	
	    	
	    	mapperFactory.classMap(LineItemDTO.class, LineItem.class)
	    	.mapNulls(false).mapNullsInReverse(true)
	    	.byDefault().register();	
	    	
	    	
	    }
	    
	    
	    public Order convertOrderDTOToEvent(OrderDTO sourceOrder, Order order) {
	    	BoundMapperFacade<OrderDTO, Order> 
	         boundMapper = mapperFactory.getMapperFacade(OrderDTO.class, Order.class);  
	        return boundMapper.map(sourceOrder,order);
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
