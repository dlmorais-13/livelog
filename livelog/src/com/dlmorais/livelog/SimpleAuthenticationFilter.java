package com.dlmorais.livelog;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

/**
 * Simple authentication filter that sets an user if cookie llst is received.
 * correctly.
 *
 * @author dlmorais (daniel.lemos.morais@gmail.com)
 */
@PreMatching
public class SimpleAuthenticationFilter implements ContainerRequestFilter {

	/** HTTP Request. */
	@Context
	private HttpServletRequest request;

	@Override
	public void filter(final ContainerRequestContext requestContext) throws IOException {
		requestContext.setSecurityContext(new SecurityContext() {

			@Override
			public boolean isUserInRole(final String role) {
				return this.getUserPrincipal() != null;
			}

			@Override
			public Principal getUserPrincipal() {
				final String secureToken = LiveLogConfig.getSecureToken();
				if (secureToken == null || "".equals(secureToken)) {
					return () -> "User";
				}

				final Cookie[] cookies = SimpleAuthenticationFilter.this.request.getCookies();
				if (cookies != null) {
					final String llstCookie = (String) SimpleAuthenticationFilter.this.request.getSession()
							.getAttribute("llst");
					final Cookie cookie = Arrays.stream(cookies).filter(c -> c.getName().equals("llst")).findFirst()
							.orElse(null);
					if (cookie != null && cookie.getValue().equals(llstCookie)) {
						return () -> "User";
					}
				}

				return null;
			}

			@Override
			public boolean isSecure() {
				return requestContext.getSecurityContext().isSecure();
			}

			@Override
			public String getAuthenticationScheme() {
				return requestContext.getSecurityContext().getAuthenticationScheme();
			}
		});
	}

}
