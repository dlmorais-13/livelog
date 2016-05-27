package com.dlmorais.livelog;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public final class LiveLogConfig {

	private static String getValueFromWebXml(String name) {
		try {
			Context env = (Context) new InitialContext().lookup("java:comp/env");
			return (String) env.lookup(name);
		} catch (NamingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getLogDir() {
		return getValueFromWebXml("livelog-logdir");
	}
	
}
