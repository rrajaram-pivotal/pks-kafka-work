package io.pivotal.workshops.pkskafka.order.domain;

import java.util.Random;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;

import io.pivotal.workshops.pkskafka.domain.events.order.Order;
import io.pivotal.workshops.pkskafka.domain.events.order.State;
import io.pivotal.workshops.pkskafka.ResourceBinding;
import io.pivotal.workshops.pkskafka.domain.events.ordervalidation.OrderValidation;
import io.pivotal.workshops.pkskafka.domain.events.ordervalidation.OrderValidationResult;
import io.pivotal.workshops.pkskafka.domain.events.ordervalidation.OrderValidationType;

public class OrderAggregator {
	
	@StreamListener
	@SendTo (ResourceBinding.ORDER_OUT)
	public KStream<String, Order> orderProcessed (@Input (ResourceBinding.ORDER_VALIDATION_STREAM_IN) KStream<String, OrderValidation> orderValidationEventStream, @Input (ResourceBinding.ORDER_STREAM_IN) KStream<String, Order> orderEventStream
			)
	{
		KStream<String,Order> outputStream = null;
		
		try {
			
			outputStream = 
					orderValidationEventStream
		        .groupByKey(serdes3)
		        .windowedBy(SessionWindows.with(Duration.ofMinutes(5)))
		        .aggregate(
		            () -> 0L,
		            (id, result, total) -> PASS.equals(result.getValidationResult()) ? total + 1 : total,
		            (k, a, b) -> b == null ? a : b, //include a merger as we're using session windows.
		            Materialized.with(null, Serdes.Long())
		        )
		        //get rid of window
		        .toStream((windowedKey, total) -> windowedKey.key())
		        //When elements are evicted from a session window they create delete events. Filter these.
		        .filter((k1, v) -> v != null)
		        //only include results were all rules passed validation
		        .filter((k, total) -> total >= numberOfRules)
		        //Join back to orders
		        .join(orders, (id, order) ->
		                //Set the order to Validated
		                newBuilder(order).setState(VALIDATED).build()
		            , JoinWindows.of(Duration.ofMinutes(5)), serdes4)
		        //Push the validated order into the orders topic
		        .to(ORDERS.name(), serdes5);

		    //If any rule fails then fail the order
		    validations.filter((id, rule) -> FAIL.equals(rule.getValidationResult()))
		        .join(orders, (id, order) ->
		                //Set the order to Failed and bump the version on it's ID
		                newBuilder(order).setState(OrderState.FAILED).build(),
		            JoinWindows.of(Duration.ofMinutes(5)), serdes7)
		        //there could be multiple failed rules for each order so collapse to a single order
		        .groupByKey(serdes6)
		        .reduce((order, v1) -> order)
		        //Push the validated order into the orders topic
		        .toStream().to(ORDERS.name(), Produced.with(ORDERS.keySerde(), ORDERS.valueSerde()));
	
			outputStream = orderEventStream.filter((key,value) -> value.getState().equals(State.placed))
								.mapValues(value -> {
									boolean creditValidated = true;
									boolean random = (randomGenerator.nextInt(2) == 0) ? true : false;
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
