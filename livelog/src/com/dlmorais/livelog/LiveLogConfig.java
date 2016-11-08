package com.dlmorais.livelog;

import java.io.File;
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
	private static String getValueFromWebXml(final String name, final boolean ignoreName) {
		try {
			final Context env = (Context) new InitialContext().lookup("java:comp/env");
			return (String) env.lookup(name);
		} catch (final NamingException e) {
			if (!ignoreName) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
			return null;
		}
	}

	/**
	 * Returns the defined secure token.
	 *
	 * @return {@link String} with the defined secure token.
	 */
	public static String getSecureToken() {
		return LiveLogConfig.getValueFromWebXml("livelog-secure-token", true);
	}

	/**
	 * Returns the directory of the log files.
	 *
	 * @return {@link String} with the directory of the log files.
	 */
	public static String getLogDir() {
		if (LiveLogConfig.LOG_DIR == null) {
			LiveLogConfig.LOG_DIR = Optional.ofNullable(LiveLogConfig.getValueFromWebXml("livelog-logdir", false));
		}
		return LiveLogConfig.LOG_DIR.orElse(null);
	}

	/**
	 * Returns the path to a log file.
	 *
	 * @param fileName
	 *            Name of the file.
	 * @return {@link String} with the path to the log file.
	 */
	public static String getLogFilePath(final String fileName) {
		final String logDir = LiveLogConfig.getLogDir();
		final StringBuilder sb = new StringBuilder(logDir);
		if (!logDir.endsWith("/") && !logDir.endsWith(File.separator)) {
			sb.append(File.separator);
		}
		sb.append(fileName);
		return sb.toString();
	}

	/**
	 * Returns the content regex to get real content on the log files.
	 *
	 * @return {@link String} with the content regex getter.
	 */
	public static String getContentRegex() {
		if (LiveLogConfig.CONTENT_REGEX == null) {
			LiveLogConfig.CONTENT_REGEX = Optional
					.ofNullable(LiveLogConfig.getValueFromWebXml("livelog-contentregex", true));
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
		while ((name = LiveLogConfig.getValueFromWebXml("livelog-customgroup-" + counter + "-name", true)) != null) {
			final String color = LiveLogConfig.getValueFromWebXml("livelog-customgroup-" + counter + "-color", false);
			final String regex = LiveLogConfig.getValueFromWebXml("livelog-customgroup-" + counter + "-regex", false);

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
