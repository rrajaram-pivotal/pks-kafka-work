package io.pivotal.workshops.pkskafka.order.order.domain;

import lombok.Data;

@Data
public class LineItemDTO {
	
	private String sku;
	private int lineNumber;
	private String state;
	private float purchasePrice;
	private String upc;
	private String estimatedDeliveryDate;
	private String orderID;

}
