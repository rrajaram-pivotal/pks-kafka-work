package com.example.demo;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.example.demo.config.ApplicationConfig;

@SpringBootApplication
public class DemoApplication {

	private static Logger log = LoggerFactory.getLogger(DemoApplication.class);
	
	public static void main(String[] args) {
		
		
		log.info("Starting Demo Application ");
		Class[] configClasses = {DemoApplication.class, ApplicationConfig.class};
		ApplicationContext ctx = SpringApplication.run(configClasses, args);
		

		log.info("Initialized config classes, JAX-RS server endpoint & resources as Spring Beans");

        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            log.info(beanName);
        }

	}


}

