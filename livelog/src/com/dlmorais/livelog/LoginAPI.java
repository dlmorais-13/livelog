package com.dlmorais.livelog;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/login")
public class LoginAPI {

	/** Contexto da requisição da Servlet. */
	@Context
	private HttpServletRequest servletRequest;

	@GET
	@Path("/enabled")
	public Response loginEnabled() {
		final String secureToken = LiveLogConfig.getSecureToken();
		return Response.ok(secureToken != null && !"".equals(secureToken)).build();
	}

	/**
	 * Evaluates the secure token and sets the auth session token if valid.
	 *
	 * @param token
	 *            Token to be evaluated.
	 * @return {@link Response} with {@link Status#OK} and a set-cookie header
	 *         or {@link Status#UNAUTHORIZED}.
	 */
	@GET
	public Response login(@QueryParam("t") final String token) {
		final String secureToken = LiveLogConfig.getSecureToken();
		final boolean allowed = secureToken == null || "".equals(secureToken) || secureToken.equals(token);
		if (allowed) {
			final String randomToken = Double.valueOf(Math.random()).toString();
			this.servletRequest.getSession().setAttribute("llst", randomToken);
			return Response.ok().cookie(new NewCookie("llst", randomToken)).build();
		}

		return Response.status(Status.UNAUTHORIZED).build();
	}

}
