package com.dlmorais.livelog;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * API to tail the log file.
 *
 * @author dlmorais (daniel.lemos.morais@gmail.com)
 */
@Path("/tail")
@Produces(MediaType.APPLICATION_JSON)
public class TailerAPI {

	/**
	 * Tails the file with name <code>file</code>. <br>
	 * <br>
	 * If the parameter <code>fromLine</code> is defined, the tail starts from
	 * this defined line and the parameter <code>numberOfLines</code> is
	 * ignored. <br>
	 * <br>
	 * If the parameter <code>numberOfLines</code> is defined, this maximum
	 * number of lines is returned from before the end of file, just like
	 * <code>tail -f -n [number]</code>.
	 *
	 * @param file
	 *            name of the file to tail from.
	 * @param fromLine
	 *            start line to tail from.
	 * @param numberOfLines
	 *            number of line to tail from the end of file.
	 * @return {@link Response} with a {@link List} of {@link LogLineDTO}
	 *         containing the tail information.
	 * @throws IOException
	 *             if there is an error when reading the log file.
	 */
	@GET
	public Response get(@QueryParam("f") final String file, @QueryParam("l") final Integer fromLine,
			@QueryParam("n") final Integer numberOfLines) throws IOException {

		final List<LogLineDTO> content = new ArrayList<>();
		Stream<String> lines = null;
		try {
			// Get all the lines from log into a stream.
			lines = Files.lines(Paths.get(LiveLogConfig.getLogDir() + file));

			// Defines the start line.
			long startLine = 0;
			// If passed as parameter, uses it.
			if (fromLine != null) {
				startLine = fromLine - 1;
			} else {
				// Uses the end of the file minus the number of lines otherwise.

				// Number of lines to tail from end of file. Default 100.
				final Integer numLines = Optional.ofNullable(numberOfLines).orElse(100);

				startLine = lines.parallel().mapToLong(l -> 1L).sum() - numLines;

				// It's necessary to reread the lines, as they are already
				// streamed.
				lines.close();
				lines = Files.lines(Paths.get(LiveLogConfig.getLogDir() + file));

				// TODO dlmorais - 13 de jun de 2016 - check if it is possible
				// to evict reread the lines.
			}

			// Parse each line to a DTO and add to list.
			long line = Math.max(startLine, 0);
			lines.skip(line).forEach(l -> {
				final LogLineDTO dto = new LogLineDTO();
				dto.setContent(l);
				content.add(dto);
			});

			// Sets the line number of each DTO.
			// TODO dlmorais - 13 de jun de 2016 - check if it is possible to
			// avoid double loop.
			for (final LogLineDTO logLineDTO : content) {
				logLineDTO.setLine(++line);
			}
		} finally {
			if (lines != null) {
				lines.close();
			}
		}

		return Response.ok(content).build();
	}

}
