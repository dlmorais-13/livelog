package com.dlmorais.livelog;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * API to show analytics grouped information on the opened log file.
 *
 * @author dlmorais (daniel.lemos.morais@gmail.com)
 */
@Path("/analytics")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed("")
public class AnalyticTableAPI {

	/**
	 * Returns the data from the log file with name <code>file</code> grouped
	 * using the regex defined on web.xml.
	 *
	 * @param file
	 *            name of the file to analise.
	 * @return {@link Response} with grouped data.
	 */
	@GET
	public Response get(@QueryParam("f") final String file) {
		final Map<String, AnalyticDataDTO> analysis = new HashMap<>();

		Stream<String> lines = null;
		try {
			final Pattern pattern = Pattern.compile(LiveLogConfig.getContentRegex());

			// Get all the lines from log into a stream.
			lines = Files.lines(Paths.get(LiveLogConfig.getLogDir() + file));
			lines.forEach(l -> {
				final Matcher matcher = pattern.matcher(l);
				if (matcher.find()) {
					final String group = matcher.group(1);
					analysis.merge(group, new AnalyticDataDTO(group, 1L), (v1, v2) -> {
						v1.increment();
						return v1;
					});
				}
			});
			return Response.ok(analysis.values()).build();
		} catch (final Exception e) {
			System.err.println(e.getStackTrace());
			return Response.noContent().build();
		} finally {
			if (lines != null) {
				lines.close();
			}
		}
	}

}
