package com.dlmorais.livelog;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

/**
 * Jersey application configuration. <br>
 * Only loads this package to configure the APIs.
 *
 * @author dlmorais (daniel.lemos.morais@gmail.com)
 */
@ApplicationPath("/livelog/api/*")
public class Application extends ResourceConfig {

	/**
	 * Class constructor loading the package of this class.
	 */
	public Application() {
		this.packages(this.getClass().getPackage().getName());
		this.register(RolesAllowedDynamicFeature.class);
		this.register(SimpleAuthenticationFilter.class);
	}

}
