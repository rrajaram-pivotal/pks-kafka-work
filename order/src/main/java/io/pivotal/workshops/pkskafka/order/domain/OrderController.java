package io.pivotal.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.cloud.stream.schema.client.ConfluentSchemaRegistryClient;
import org.springframework.cloud.stream.schema.client.SchemaRegistryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.pivotal.order.model.OrderBean;
import io.pivotal.order.model.avro.Order;

@RestController
@RequestMapping("/v1")
//@EnableSchemaRegistryClient
//@EnableBinding(Source.class)
public class OrderController {

 /*   @Autowired
    private KafkaTemplate<String, Order> orderKafkaTemplate;*/
    
/*    @Value(value = "${kafka.producer.topic.orders}")
    private String orderTopicName;*/
    
	@Autowired
	private Source source;
	
    //private final MessageChannel orderOut;
    
    /**
	 * Constructor with bindings for consuming the output channel to the "orders" topic
	 * @param binding
	 */
/*	public OrderController(KafkaOrderStreamBinding binding) {

		this.orderOut = binding.orderOut();
	}*/
    
	@GetMapping("/orders/{id}")
	public OrderBean getOrder(@PathVariable(name="id") String id) {
			
		OrderBean order = new OrderBean();
		order.setId("1");
		order.setCustomerId(2L);
		order.setState("CREATED");
		order.setQuantity(2);
		order.setProduct("pen");
		order.setPrice(5.00);
		
		return order;
		
	}
	
	
/*	@PostMapping("/orders")
	@Consumes(MediaType.APPLICATION_JSON)
	public void streamOrder(@RequestBody OrderBean orderbean) {
		
		System.out.println("OrderID:" + orderbean.getId());
		System.out.println("OrderBean:" + orderbean.toString());
		
		Order order = Order.newBuilder()
				.setId(orderbean.getId())
				.setCustomerId(orderbean.getCustomerId())
				.setState("CREATED")
				.setProduct(orderbean.getProduct())
				.setQuantity(orderbean.getQuantity())
				.setPrice(orderbean.getPrice())
				.build();
		
ListenableFuture<SendResult<String, Order>> future = orderKafkaTemplate.send(orderTopicName, order);
        
        future.addCallback(new ListenableFutureCallback<SendResult<String, Order>>() {

            @Override
            public void onSuccess(SendResult<String, Order> result) {
                System.out.println("Order Saved with offset=[" + result.getRecordMetadata().offset() + "]");
            }
            @Override
            public void onFailure(Throwable ex) {
                System.out.println("Unable to save order due to : " + ex.getMessage());
            }

        });

	}*/
	
	@PostMapping("/orderstream")
	//@Consumes(MediaType.APPLICATION_JSON)
	public void streamOrders(@RequestBody OrderBean orderbean) {
		
		System.out.println("OrderID:" + orderbean.getId());
		System.out.println("OrderBean:" + orderbean.toString());
		
		Order order = Order.newBuilder()
				.setId(orderbean.getId())
				.setCustomerId(orderbean.getCustomerId())
				.setState("CREATED")
				.setProduct(orderbean.getProduct())
				.setQuantity(orderbean.getQuantity())
				.setPrice(orderbean.getPrice())
				.build();
		
/*		Message<Order> message = MessageBuilder.withPayload(order)
				.setHeader("kafka_messageKey", order.getId())
				//.setHeader("contentType", "application/*+avro")
				.build();*/
		//this.orderOut.send(message);
		source.output().send(org.springframework.messaging.support.MessageBuilder.withPayload(order).build());
		//source.output().send(message);


	}
	
	@Configuration
	static class ConfluentSchemaRegistryConfiguration {
		@Bean
		public SchemaRegistryClient schemaRegistryClient(@Value("${spring.cloud.stream.schemaRegistryClient.endpoint}") String endpoint){
			ConfluentSchemaRegistryClient client = new ConfluentSchemaRegistryClient();
			client.setEndpoint(endpoint);
			return client;
		}
	}
	
}
