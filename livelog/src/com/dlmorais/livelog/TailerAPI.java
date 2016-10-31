package com.dlmorais.livelog;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.LongAdder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * API to tail the log file.
 *
 * @author dlmorais (daniel.lemos.morais@gmail.com)
 */
@Path("/tail")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed("")
public class TailerAPI {

	/** Injected request to store data to the session. */
	@Context
	private HttpServletRequest request;

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

		this.defineCurrentFile(file);

		Stream<String> lines = null;
		try {
			// Get all the lines from log into a stream.
			lines = Files.lines(Paths.get(LiveLogConfig.getLogDir() + file));

			// Defines the start line.
			long startLine = 0;
			// If passed as parameter, uses it.
			if (fromLine != null) {
				startLine = fromLine - 1;
			}

			final List<LogLineDTO> content = new ArrayList<>();

			// Number of lines to tail from end of file. Default 100.
			final Integer numLines = Optional.ofNullable(numberOfLines).orElse(100);

			final List<CustomGroupDTO> customGroupings = this.getGroupings();

			final LongAdder adder = new LongAdder();
			adder.add(startLine);

			lines.skip(startLine).forEach(l -> {
				// For each grouping, verify if the line matches the
				// grouping and add a counter on it.
				for (final CustomGroupDTO customGroupDTO : customGroupings) {
					final Matcher matcher = Pattern.compile(customGroupDTO.getRegex()).matcher(l);
					if (matcher.find()) {
						customGroupDTO.increment();
					}
				}

				adder.increment();
				final LogLineDTO dto = new LogLineDTO();
				dto.setLine(adder.sum());
				dto.setContent(l);

				// Adds the DTO do return list and removes the first if it is
				// bigger than the maximum size.
				content.add(dto);
				if (fromLine == null && content.size() > numLines) {
					content.remove(0);
				}
			});

			return Response.ok(content).build();
		} catch (final Exception e) {
			System.err.println(e.getStackTrace());
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		} finally {
			if (lines != null) {
				lines.close();
			}
		}
	}

	/**
	 * Returns the custom groupings list from session. <br>
	 * This method will initialize the list in the session, if not present.
	 *
	 * @return {@link List} of {@link CustomGroupDTO} from session.
	 */
	private List<CustomGroupDTO> getGroupings() {
		final HttpSession session = this.request.getSession();
		@SuppressWarnings("unchecked")
		List<CustomGroupDTO> customGroupings = (List<CustomGroupDTO>) session.getAttribute("customGroupings");
		if (customGroupings == null) {
			customGroupings = LiveLogConfig.getCustomGroupings();
			session.setAttribute("customGroupings", customGroupings);
		}
		return customGroupings;
	}

	/**
	 * Defines the current opened file and reload the necessary data, if
	 * changed.
	 *
	 * @param file
	 *            name of the polled file.
	 */
	private void defineCurrentFile(final String file) {
		final HttpSession session = this.request.getSession();
		String currentFile = (String) session.getAttribute("livelog-currentfile");
		if (currentFile == null || !currentFile.equals(file)) {
			currentFile = file;
			session.setAttribute("livelog-currentfile", currentFile);
			session.removeAttribute("customGroupings");
		}
	}

}
