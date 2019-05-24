package io.pivotal.workshops.pkskafka.queryorder.domain;

import lombok.Data;

@Data
public class LineItemDTO {
	
	private String sku;
	private int lineNumber;
	private String state;
	private float purchasePrice;
	private String upc;
	private String estimatedDeliveryDate;

}
