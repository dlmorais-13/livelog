package com.dlmorais.livelog;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Collectors;

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
		File logDir = new File(LiveLogConfig.getLogDir());
		File[] files = logDir.listFiles();
		return Response.ok(
					Arrays.stream(files)
					.map(f -> f.getName())
					.collect(Collectors.toList()))
				.build();
	}
	
}
