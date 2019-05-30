package io.pivotal.workshops.pkskafka.order.order.domain;

import java.util.List;

import lombok.Data;

@Data
public class CustomerDTO {
	
	private String firstName;
	private String lastName;
	private boolean automatedEmail;
	private List<String> customerEmails;
	private List<CustomerAddressDTO> customerAddress;

}
