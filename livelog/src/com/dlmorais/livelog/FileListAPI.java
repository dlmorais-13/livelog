package com.dlmorais.livelog;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * API to list the available log files.
 *
 * @author dlmorais (daniel.lemos.morais@gmail.com)
 */
@Path("/list-files")
@Produces(MediaType.APPLICATION_JSON)
public class FileListAPI {

	/**
	 * Returns a {@link Response} containing a {@link List} of {@link String}
	 * with the available log file names.
	 *
	 * @return {@link Response} with {@link List} of {@link String} containing
	 *         the log file names.
	 */
	@GET
	public Response get() {
		final File logDir = new File(LiveLogConfig.getLogDir());
		final File[] files = logDir.listFiles();
		return Response.ok(Arrays.stream(files).map(f -> f.getName()).collect(Collectors.toList())).build();
	}

}
