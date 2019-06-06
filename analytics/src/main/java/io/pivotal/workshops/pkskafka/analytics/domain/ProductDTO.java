package io.pivotal.workshops.pkskafka.analytics.domain;


import lombok.Data;


@Data
public class ProductDTO {
	
	private String productSku;
	private Long orderCount;

}
