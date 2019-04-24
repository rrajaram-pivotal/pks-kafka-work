package com.example.demo.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path (value="/hello")
@Consumes (MediaType.TEXT_PLAIN)
@Produces (MediaType.TEXT_PLAIN)
public interface HelloService {
	
	
	@GET
	@Path ("/{name}")
	public String sayHello(@PathParam("name") String name);
	
	@POST
	public void postHello();
	

}