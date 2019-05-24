package io.pivotal.workshops.pkskafka.queryorder.domain;

import java.util.List;


import lombok.Data;

@Data
public class OrderDTO {
	
	private String orderID;
	private String timePlaced;
	private String lastUpdated;
	private String state;
	private CustomerDTO customer;
	private List<LineItemDTO> lineItems;
	
	
	
	

}
