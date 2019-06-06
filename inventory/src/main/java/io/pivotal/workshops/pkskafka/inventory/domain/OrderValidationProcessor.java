package io.pivotal.workshops.pkskafka.inventory.domain;



import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Aggregator;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.Initializer;
import org.apache.kafka.streams.kstream.Joined;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.Serialized;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.kstream.ValueJoiner;
import org.apache.kafka.streams.kstream.ValueMapper;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.cloud.stream.schema.client.ConfluentSchemaRegistryClient;
import org.springframework.cloud.stream.schema.client.SchemaRegistryClient;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import io.pivotal.workshops.pkskafka.inventory.InventoryBindings;

import io.pivotal.workshops.pkskafka.domain.events.inventory.Inventory;
import io.pivotal.workshops.pkskafka.domain.events.order.LineItem;
import io.pivotal.workshops.pkskafka.domain.events.order.LineItemState;
import io.pivotal.workshops.pkskafka.domain.events.order.Order;
import io.pivotal.workshops.pkskafka.domain.events.order.State;
import io.pivotal.workshops.pkskafka.domain.events.ordervalidation.OrderValidation;
import io.pivotal.workshops.pkskafka.domain.events.ordervalidation.OrderValidationResult;
import io.pivotal.workshops.pkskafka.domain.events.ordervalidation.OrderValidationType;
import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
@Component
public class OrderValidationProcessor {

	@Value("${spring.cloud.stream.schemaRegistryClient.endpoint}") 
	private String schemaRegistryEndpoint;

	
	@StreamListener
	@SendTo (InventoryBindings.ORDER_VALIDATION_STREAM_OUT)
	public KStream<String, OrderValidation> validateOrder (
			@Input (InventoryBindings.ORDER_STREAM_IN) KStream<String, Order> orderEventStream, //Order Input Stream
			@Input (InventoryBindings.INVENTORY_IN) GlobalKTable<String, Inventory> inventoryStream) //Inventory Stream Materialized as GlobalKTable
	{
		KStream <String, OrderValidation> outputStream=null;
		KStream<String,LineItem> lineItemStream = null;
		
		try {
			
			orderEventStream.foreach((key,value) -> log.info("Order Details " + value.toString()));
			
			lineItemStream = orderEventStream
					.filter((key,value) -> value.getState().equals(State.placed))
					.flatMapValues(value -> value.getLineItems())
					.selectKey((orderId, lineItem) -> lineItem.getSku().toString())
					.join(inventoryStream,
							new KeyValueMapper<String, LineItem, String>() {

								@Override
								public String apply(String key, LineItem value) {
									log.info("Sku Key " + key.toString());
									return key;
								}	
							}, 	
						new ValueJoiner<LineItem, Inventory, LineItem>() {
							@Override
							public LineItem apply(LineItem value1, Inventory value2) {
								log.info("Value From Line Item " + value1.getSku() + " Value 2 " + value2.getSku() + " Inventory " + value2.getQuantity() );
								if (value2.getQuantity() > 0)
								{
									value1.setState(LineItemState.validated);
								}
								else
								{
									value1.setState(LineItemState.insufficient_inventory);
								}
								return value1;
							}
					});
			log.info("Schema Registry URL " + schemaRegistryEndpoint);
			Map<String, String> props = new HashMap<>();
			props.put("schema.registry.url",schemaRegistryEndpoint );
			
			SpecificAvroSerde<LineItem> lineItemSerde = new SpecificAvroSerde<LineItem>();
			lineItemSerde.serializer().configure(props, false);
			lineItemSerde.deserializer().configure(props, false);
			
			SpecificAvroSerde<OrderValidation> orderValSerde = new SpecificAvroSerde<OrderValidation>();
			orderValSerde.serializer().configure(props, false);
			orderValSerde.deserializer().configure(props, false);

			SpecificAvroSerde<Order> orderSerde = new SpecificAvroSerde<Order>();
			orderSerde.serializer().configure(props, false);
			orderSerde.deserializer().configure(props, false);

			SpecificAvroSerde<Inventory> inventorySerde = new SpecificAvroSerde<Inventory>();
			inventorySerde.serializer().configure(props, false);
			inventorySerde.deserializer().configure(props, false);
			
			lineItemStream.foreach((key,value) -> log.info("Line Item Stream in Inventory Processor " + key + " -" + value.toString()));
			
			
			outputStream = 
					lineItemStream
				.selectKey((sku, lineItem) -> lineItem.getOrderID().toString())
				//.filter((key,value) -> value.getState().equals(LineItemState.insufficient_inventory))
				
				.groupByKey(Serialized.with(Serdes.String(), lineItemSerde))
				.aggregate(new Initializer<OrderValidation>() {

					@Override
					public OrderValidation apply() {
						
						return new OrderValidation();
					}
					
					}, new Aggregator<String, LineItem, OrderValidation>() {

					@Override
					public OrderValidation apply(String key, LineItem value, OrderValidation aggregate) {
						
						if (aggregate.getOrderId() == null || aggregate.getOrderId().length()==0)
						{
							log.info("Setting Aggregate Values ");
							
							aggregate.setOrderId(key);
							aggregate.setCheckType(OrderValidationType.INVENTORY_CHECK);
							aggregate.setValidationResult(OrderValidationResult.PASS);
							log.info("Set Aggregate Values " + aggregate.getOrderId() );
						}
						
						
						if (value.getState().equals(LineItemState.insufficient_inventory))
						{
							aggregate.setValidationResult(OrderValidationResult.FAIL);
							log.info("Set Aggregate Validation Result " + aggregate.getValidationResult() );
						}
						
						log.info("Set Aggregate Validation Result " + aggregate.getValidationResult() );
							return aggregate;
					}
					
				}, Materialized.with(Serdes.String(), orderValSerde)).toStream();
	
				//Update Inventory
			
			/*orderEventStream
			.filter((key,value) -> value.getState().equals(State.validated))
			.flatMapValues(value -> value.getLineItems())
			.selectKey((orderId, lineItem) -> lineItem.getSku().toString())
			.join(inventoryStream,
					new KeyValueMapper<String, LineItem, String>() {

						@Override
						public String apply(String key, LineItem value) {
							log.info("Sku Key " + key.toString());
							return key;
						}	
					}, 	
				new ValueJoiner<LineItem, Inventory, Inventory>() {
					@Override
					public Inventory apply(LineItem value1, Inventory value2) {
						log.info("Value From Line Item " + value1.getSku() + " - " + value1.getState() + " Value 2 " + value2.getSku() +  " Inventory " + value2.getQuantity() );
						if (value1.getState().equals(LineItemState.validated))
						{
							value2.setQuantity(value2.getQuantity()-1);
							log.info("Value From Line Item " + value2.getSku() + " Value 2 " + value2.getSku() + " Updated Inventory " + value2.getQuantity() );
						}
						
						return value2;
					}
			}).to("inventory-topic", Produced.with(Serdes.String(), inventorySerde));*/
			
			
			} catch (Exception ex) 
			{
				ex.printStackTrace();
				log.error("Exception At OrderValidationProcessor " + ex.getMessage());
			}
			return outputStream;
	}
}
