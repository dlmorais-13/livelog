package com.dlmorais.livelog;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * API to download a log file.
 *
 * @author dlmorais (daniel.lemos.morais@gmail.com)
 */
// TODO dlmorais - 13 de jun de 2016 - ZIP the log file before downloading,
// preferably without writing to disk.
@Path("/download")
@RolesAllowed("")
public class DownloadAPI {

	/**
	 * Returns a {@link Response} containing an {@link InputStream} of the log
	 * file with name <code>file</code>.
	 *
	 * @param file
	 *            name of the file to download.
	 * @return {@link Response} with the {@link InputStream} to the file.
	 * @throws IOException
	 *             if it's not possible to open the {@link InputStream} to the
	 *             file.
	 */
	@GET
	public Response get(@QueryParam("f") final String file) throws IOException {

		final String logDir = LiveLogConfig.getLogDir();

		final StringBuilder sb = new StringBuilder(logDir);
		if (!logDir.endsWith("/") && !logDir.endsWith(File.separator)) {
			sb.append(File.separator);
		}
		sb.append(file);

		final FileInputStream fis = new FileInputStream(new File(sb.toString()));
		return Response.ok(fis, MediaType.APPLICATION_OCTET_STREAM)
				.header("content-disposition", "attachment; filename = " + file).build();
	}

}
