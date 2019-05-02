package io.pivotal.workshops.pkskafka.analytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

import io.pivotal.workshops.pkskafka.analytics.bindings.AnalyticsBindings;

@SpringBootApplication
@EnableBinding (AnalyticsBindings.class)
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	
	

}
