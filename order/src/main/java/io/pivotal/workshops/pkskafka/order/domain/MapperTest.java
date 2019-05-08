package io.pivotal.workshops.pkskafka.order.domain;

import java.util.Arrays;

import io.pivotal.workshops.pkskafka.order.domain.events.Order;



public class MapperTest {
	
	public static void main (String[] args)
	{
		OrderDTO orderDTO = new OrderDTO();
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
		
		
		customerAddressDTO.setAddress("820 Balmoral Ct");
		customerAddressDTO.setCity("Glen Mills");
		customerAddressDTO.setState("PA");
		customerAddressDTO.setZipcode("12342");
		customerDTO.setFirstName("John");
		customerDTO.setLastName("Smith");
		customerDTO.setCustomerAddress(Arrays.asList(customerAddressDTO));
		customerDTO.setCustomerEmails(Arrays.asList("john.smith@google.com"));
		customerDTO.setAutomatedEmail(true);
		
		orderDTO.setLastUpdated("05/02/2019");
		orderDTO.setTimePlaced("05/02/2019");
		orderDTO.setLineItems(Arrays.asList(lineItemDTO));
		orderDTO.setCustomer(customerDTO);
		
		OrderMapper mapper = new OrderMapper();
		Order order = mapper.convertOrderDTOToEvent(orderDTO);
		
		System.out.println("Order " + order.toString());
		
		
		
	}

}
