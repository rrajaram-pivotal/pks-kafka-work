package io.pivotal.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

import io.pivotal.order.config.KafkaOrderStreamBinding;

@SpringBootApplication
@EnableBinding (KafkaOrderStreamBinding.class)
public class OrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderApplication.class, args);
	}

}
