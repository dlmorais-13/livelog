package com.dlmorais.livelog;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/download")
public class DownloadAPI {

	@GET
	public Response get(@QueryParam("f") String file) throws IOException {
		final FileInputStream fis = new FileInputStream(new File(LiveLogConfig.getLogDir() + file));
		return Response.ok(fis, MediaType.APPLICATION_OCTET_STREAM)
				.header("content-disposition", "attachment; filename = " + file + ".zip")
				.build();
	}

}
