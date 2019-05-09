package io.pivotal.workshops.pkskafka.order.domain;

import lombok.Data;

@Data
public class LineItemDTO {
	
	private String sku;
	private int lineNumber;
	private String state;
	private float originalPrice;
	private float discount;
	private float purchasePrice;
	private float estimatedUnitTax;
	private String upc;
	private String estimatedDeliveryDate;

}
