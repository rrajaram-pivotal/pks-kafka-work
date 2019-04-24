package io.pivotal.workshops.pkskafka.analytics.sources;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

import io.pivotal.workshops.pkskafka.analytics.binding.AnalyticsBindings;
import io.pivotal.workshops.pkskafka.analytics.events.OrderEvent;
import lombok.extern.apachecommons.CommonsLog;

/**
 * This class acts as source of order events. 
 * TODO: This needs to be moved to the tests for the application. 
 * @author rrajaram
 *
 */
@Component
@CommonsLog
public class OrderEventSource implements ApplicationRunner {

	private final MessageChannel orderOut;
	
	
	
	/**
	 * Constructor with bindings for consuming the output channel to the "orders" topic
	 * @param binding
	 */
	public OrderEventSource(AnalyticsBindings binding) {

		this.orderOut = binding.orderOut();
	}


	@Override
	public void run(ApplicationArguments args) throws Exception {
		
		// Random list of users, products and amount for generating order events
		List<String> userId = Arrays.asList("rrajaram", "makrishna","jsmith","rkennedy");
		List<String> productId = Arrays.asList("nike-baseball-gloves", "umbro-soccer-ball","umbro-soccer-cleats","speedo-swim-goggles");
		List<Number> amount = Arrays.asList(10.48, 20,13.20,8.99);
		
		
		Runnable runnable = () -> {
			// Random order id for each order event
			Integer orderId = new Random().nextInt();
			String rUserId = userId.get (new Random().nextInt(userId.size()));
			String rProductId = productId.get (new Random().nextInt(productId.size()));
			Number rAmount = amount.get (new Random().nextInt(amount.size()));
			
			// Create an order and publish it as an event to the "orders" topic. This would come 
			// from an Order microservice.
			OrderEvent orderEvent = new OrderEvent(orderId, rUserId, rProductId, rAmount);
			Message<OrderEvent> message = MessageBuilder.withPayload(orderEvent).setHeader(KafkaHeaders.MESSAGE_KEY, orderEvent.getUserId().getBytes()).build();
			
			
			try {
				this.orderOut.send(message);
				log.info("Sent Message -> " + message.toString());
			}
			catch (Exception ex)
			{
				log.error("Exception at OrderEventSource" + ex.toString());
			}
		};
		
		//  Executor to run the runnable task of generating orders every 5 seconds
		Executors.newScheduledThreadPool(1).scheduleAtFixedRate(runnable, 1, 5, TimeUnit.SECONDS);
		
		
	}

}
