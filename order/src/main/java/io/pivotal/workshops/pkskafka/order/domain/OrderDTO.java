package io.pivotal.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderBean {

	String id;
	long customerId;
	String state;
	String product;
	int quantity;
	Double price;
}
