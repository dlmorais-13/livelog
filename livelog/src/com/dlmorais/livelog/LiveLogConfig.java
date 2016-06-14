package com.dlmorais.livelog;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Utility class to get the configurations available to LiveLog.
 *
 * @author dlmorais (daniel.lemos.morais@gmail.com)
 */
public final class LiveLogConfig {

	/** Cache for the livelog-logdir web.xml parameter. */
	public static Optional<String> LOG_DIR = null;

	/** Cache for the livelog-contentregex web.xml parameter. */
	public static Optional<String> CONTENT_REGEX = null;

	/**
	 * Gets a {@link String} value from web.xml.
	 *
	 * @param name
	 *            name of the parameter.
	 * @return {@link String} with the parameter's value.
	 */
	private static String getValueFromWebXml(final String name) {
		try {
			final Context env = (Context) new InitialContext().lookup("java:comp/env");
			return (String) env.lookup(name);
		} catch (final NamingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Returns the directory of the log files.
	 *
	 * @return {@link String} with the directory of the log files.
	 */
	public static String getLogDir() {
		if (LiveLogConfig.LOG_DIR == null) {
			LiveLogConfig.LOG_DIR = Optional.ofNullable(LiveLogConfig.getValueFromWebXml("livelog-logdir"));
		}
		return LiveLogConfig.LOG_DIR.orElse(null);
	}

	/**
	 * Returns the content regex to get real content on the log files.
	 *
	 * @return {@link String} with the content regex getter.
	 */
	public static String getContentRegex() {
		if (LiveLogConfig.CONTENT_REGEX == null) {
			LiveLogConfig.CONTENT_REGEX = Optional.ofNullable(LiveLogConfig.getValueFromWebXml("livelog-contentregex"));
		}
		return LiveLogConfig.CONTENT_REGEX.orElse(null);
	}

	/**
	 * Returns a list of the defined groupings at web.xml.
	 *
	 * @return {@link List} with the defined {@link CustomGroupDTO}.
	 */
	public static List<CustomGroupDTO> getCustomGroupings() {
		final List<CustomGroupDTO> groupings = new ArrayList<>();

		int counter = 1;
		String name = null;
		while ((name = LiveLogConfig.getValueFromWebXml("livelog-customgroup-" + counter + "-name")) != null) {
			final String color = LiveLogConfig.getValueFromWebXml("livelog-customgroup-" + counter + "-color");
			final String regex = LiveLogConfig.getValueFromWebXml("livelog-customgroup-" + counter + "-regex");

			if (regex != null) {
				final CustomGroupDTO customGroupDTO = new CustomGroupDTO(name, regex);
				customGroupDTO.setColor(Optional.ofNullable(color).orElse("gray"));
				groupings.add(customGroupDTO);
			} else {
				System.err.println("LiveLog - Custom grouping defined without regular expression :: " + name);
			}

			counter++;
		}

		return groupings;
	}

}
