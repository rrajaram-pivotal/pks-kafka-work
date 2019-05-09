package io.pivotal.workshops.pkskafka.inventory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.schema.client.ConfluentSchemaRegistryClient;
import org.springframework.cloud.stream.schema.client.EnableSchemaRegistryClient;
import org.springframework.cloud.stream.schema.client.SchemaRegistryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.pivotal.workshops.pkskafka.inventory.domain.InventoryBindings;

@SpringBootApplication
@EnableSchemaRegistryClient
@EnableBinding (InventoryBindings.class)
public class InventoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryApplication.class, args);
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
