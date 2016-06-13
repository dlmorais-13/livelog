package com.dlmorais.livelog;

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
	public static String LOG_DIR = null;

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
			LiveLogConfig.LOG_DIR = LiveLogConfig.getValueFromWebXml("livelog-logdir");
		}
		return LiveLogConfig.LOG_DIR;
	}

}
