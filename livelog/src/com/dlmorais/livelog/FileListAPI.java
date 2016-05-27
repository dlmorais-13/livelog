package com.dlmorais.livelog;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/list-files")
@Produces(MediaType.APPLICATION_JSON)
public class FileListAPI {

	@GET
	public Response get() {
		return Response.ok(LiveLogConfig.getLogDir()).build();
	}
	
}
