package com.dlmorais.livelog;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * API to show grouping information on the opened log file.
 *
 * @author dlmorais (daniel.lemos.morais@gmail.com)
 */
@Path("/grouping")
@Produces(MediaType.APPLICATION_JSON)
public class CustomGroupAPI {

	/** Injected request to store data to the session. */
	@Context
	private HttpServletRequest request;

	/**
	 * Returns the count of each group defined in web.xml.
	 *
	 * @return {@link Response} with a {@link Map} with the counts.
	 */
	@GET
	public Response get() {
		final HttpSession session = this.request.getSession();
		return Response.ok(session.getAttribute("customGroupings")).build();
	}

	/**
	 * Resets the groupings.
	 *
	 * @return {@link Response} with {@link Status#NO_CONTENT}.
	 */
	@POST
	@Path("/reset")
	public Response reset() {
		this.request.getSession().removeAttribute("customGroupings");
		return Response.noContent().build();
	}

}
