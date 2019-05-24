package io.pivotal.workshops.pkskafka.queryorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.schema.client.EnableSchemaRegistryClient;

@SpringBootApplication
@EnableSchemaRegistryClient
@EnableBinding (ResourceBindings.class)
public class QueryOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(QueryOrderApplication.class, args);
	}

}
