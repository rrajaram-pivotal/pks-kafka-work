package io.pivotal.workshops.pkskafka.credit.domain;



import java.util.Iterator;
import java.util.Random;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import io.pivotal.workshops.pkskafka.credit.CreditBindings;
import io.pivotal.workshops.pkskafka.domain.events.order.LineItem;
import io.pivotal.workshops.pkskafka.domain.events.order.Order;
import io.pivotal.workshops.pkskafka.domain.events.order.State;
import io.pivotal.workshops.pkskafka.domain.events.ordervalidation.OrderValidation;
import io.pivotal.workshops.pkskafka.domain.events.ordervalidation.OrderValidationResult;
import io.pivotal.workshops.pkskafka.domain.events.ordervalidation.OrderValidationType;
import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
@Component
public class CreditProcessor {
	
	
	@StreamListener
	@SendTo (CreditBindings.ORDER_VALIDATION_STREAM_OUT)
	public KStream<String, OrderValidation> validateOrder (@Input (CreditBindings.ORDER_STREAM_IN) KStream<String, Order> orderEventStream
			)
	{
		KStream<String,OrderValidation> outputStream = null;
		
		try {
			
			Random randomGenerator = new Random();
			OrderValidation ov = new OrderValidation();


			outputStream = orderEventStream.filter((key,value) -> value.getState().equals(State.placed))
								.mapValues(value -> {
									boolean creditValidated = true;
									//boolean random = true;
									boolean random = (randomGenerator.nextInt(2) == 1) ? true : false;
									if (random == false) {
										creditValidated=false;
											log.info("Credit Validation Failed For Customer: " + value.getCustomer().getFirstName() + " " + value.getCustomer().getLastName() + " Random Value: " + random);
										}
										else {
											
											log.info("Credit Validation Successfull For Customer: " + value.getCustomer().getFirstName() + " " + value.getCustomer().getLastName() + " Random Value: " + random);
										}										
																				
									if (creditValidated == true)
									{
										ov.setCheckType(OrderValidationType.CREDIT_CHECK);
										ov.setOrderId(value.getOrderID());
										ov.setValidationResult(OrderValidationResult.PASS);
									}
									else
									{
										ov.setCheckType(OrderValidationType.CREDIT_CHECK);
										ov.setOrderId(value.getOrderID());
										ov.setValidationResult(OrderValidationResult.FAIL);
									}
									return ov;
									});	
			
			outputStream.foreach((key,value)->log.info("Validating Order " + key.toString()));
						
			
			} catch (Exception ex) 
			{
				ex.printStackTrace();
				log.error(ex.getMessage());
			}
			return outputStream;
	}
}
