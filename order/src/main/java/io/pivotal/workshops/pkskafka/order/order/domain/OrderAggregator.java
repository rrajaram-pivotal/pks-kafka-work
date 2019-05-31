package io.pivotal.workshops.pkskafka.order.order.domain;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Aggregator;
import org.apache.kafka.streams.kstream.Initializer;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.Joined;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Predicate;
import org.apache.kafka.streams.kstream.Serialized;
import org.apache.kafka.streams.kstream.SessionWindows;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.ValueJoiner;
import org.apache.kafka.streams.kstream.Window;
import org.apache.kafka.streams.kstream.Windowed;
import org.apache.kafka.streams.kstream.internals.TimeWindow;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import io.pivotal.workshops.pkskafka.domain.events.order.LineItem;
import io.pivotal.workshops.pkskafka.domain.events.order.LineItemState;
import io.pivotal.workshops.pkskafka.domain.events.order.Order;
import io.pivotal.workshops.pkskafka.domain.events.order.State;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import io.pivotal.workshops.pkskafka.domain.events.ordervalidation.OrderValidation;
import io.pivotal.workshops.pkskafka.domain.events.ordervalidation.OrderValidationResult;
import io.pivotal.workshops.pkskafka.domain.events.ordervalidation.OrderValidationType;
import io.pivotal.workshops.pkskafka.order.ResourceBinding;
import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
@Component
public class OrderAggregator {
	
	@Value("${spring.cloud.stream.schemaRegistryClient.endpoint}") 
	private String schemaRegistryEndpoint;
	
	@StreamListener
	@SendTo (ResourceBinding.ORDER_STREAM_OUT)
	public KStream<String, Order> aggregateOrderValidation (
			@Input (ResourceBinding.ORDER_VALIDATION_STREAM_IN) KStream<String, OrderValidation> orderValidationEventStream, 
			@Input (ResourceBinding.ORDER_AGG_STREAM_IN) KStream<String, Order> orderKStream
			)
	{
		KStream<String,Order> outputStream = null;
		
		try {
			
			Map<String, String> props = new HashMap<>();
			props.put("schema.registry.url",schemaRegistryEndpoint );
			
			SpecificAvroSerde<OrderValidation> orderValSerde = new SpecificAvroSerde<OrderValidation>();
			orderValSerde.serializer().configure(props, false);
			orderValSerde.deserializer().configure(props, false);		
			
			SpecificAvroSerde<Order> orderSerde = new SpecificAvroSerde<Order>();
			orderSerde.serializer().configure(props, false);
			orderSerde.deserializer().configure(props, false);	
			
			orderValidationEventStream.foreach((key,value)-> log.info("Order Aggregator ----------------->"+key+"-"+value.toString()));
			
			KStream<String, Long> interimStream =	orderValidationEventStream
					.groupByKey(Serialized.with(Serdes.String(),orderValSerde))
					.windowedBy(SessionWindows.with(Duration.ofMinutes(5).toMinutes()))
					.aggregate(new Initializer<Long>() {

						@Override
						public Long apply() {
							
							return 0L;
						}
						
						}, new Aggregator<String, OrderValidation, Long>() {

							@Override
							public Long apply(String key, OrderValidation value, Long total) {
								
								if (OrderValidationResult.PASS.equals(value.getValidationResult())) {
									 total = total+1;
								}
								return total;
							}
							
						}, (k, a, b) -> b == null ? a : b
					, Materialized.with(Serdes.String(), Serdes.Long()))
					.toStream(new KeyValueMapper<Windowed<String>, Long, String>() {

						@Override
						public String apply(Windowed<String> key, Long value) {
							
							return key.key();
						}
						
					})
					.filter((k1, v) -> v != null)
					//.filter((k, total) -> {total >= 1; log.info("")})
					.filter(new Predicate<String, Long>() {

						@Override
						public boolean test(String key, Long value) {
							log.info("Filter in Order Aggregator   ----------------> " + key);
							return value >=1;
						}
						
					});
			
			//interimStream.foreach((key,value)->log.info("Interim Stream --------------> " + key + " - " + value));
			outputStream =  orderKStream.join(interimStream, new ValueJoiner<Order, Long, Order>() {

						@Override
						public Order apply(Order value1, Long value2) {
							if (value2 == 2)
								value1.setState(State.validated);
							else
								value1.setState(State.failed_validation);
													return value1;
						}
			        	
					}, JoinWindows.of(Duration.ofMinutes(5).toMinutes()), Joined.with(Serdes.String(), orderSerde,Serdes.Long()));
			
			/*outputStream = interimStream
			        .join(orderKStream, new ValueJoiner<Long, Order, Order>() {

						@Override
						public Order apply(Long value1, Order value2) {
							if (value1 == 2)
								value2.setState(State.validated);
							else
								value2.setState(State.failed_validation);
							return value2;
						}
			        	
					}, JoinWindows.of(Duration.ofMinutes(5).toMinutes()), Joined.with(Serdes.String(), Serdes.Long(), orderSerde));*/
	                	
					
				
					//outputStream.foreach((key,value)->log.info("Order Validation Aggregator " + key + " - " + value));
			
			
			} catch (Exception ex) 
			{
				ex.printStackTrace();
				log.error(ex.getMessage());
			}
			return outputStream;
	}

}
