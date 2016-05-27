package com.dlmorais.livelog;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/livelog/api/*")
public class Application extends ResourceConfig {

	public Application() {
		this.packages("com.dlmorais.livelog");
	}
	
}
