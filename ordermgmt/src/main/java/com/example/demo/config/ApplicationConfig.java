package com.example.demo.config;

import java.util.Arrays;

import org.apache.cxf.Bus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.swagger.Swagger2Feature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.service.HelloServiceImpl;

@Configuration
public class ApplicationConfig {
	
	 @Autowired
	 private Bus bus;
	 
	 @Bean
	 public Server rsServer() {
		JAXRSServerFactoryBean endpoint = new JAXRSServerFactoryBean();
		endpoint.setBus(bus);
		endpoint.setAddress("/");
		endpoint.setServiceBeans(Arrays.<Object>asList(new HelloServiceImpl()));
		endpoint.setFeatures(Arrays.asList(new Swagger2Feature()));
		return endpoint.create();
    }


}
