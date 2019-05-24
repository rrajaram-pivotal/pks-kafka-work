package io.pivotal.workshops.pkskafka.queryorder.domain;

import lombok.Data;

@Data
public class CustomerAddressDTO {
	
	private String address;
	private String city;
	private String state;
	private String zipcode;
	
	

}
