package fi.turku.mip.reportserver.util;

import javax.naming.NamingException;

import org.springframework.jndi.JndiLocatorSupport;

/**
 * Utility class for JNDI environment variable lookups
 *
 * @author mip
 *
 */
public class JndiEnvironmentVariableLookup extends JndiLocatorSupport {

	public JndiEnvironmentVariableLookup() {
		setResourceRef(true);
	}

	public String getEnvironmentVariable(String name) {
		try {
			return lookup(name, String.class);
		} catch (NamingException ex) {
			throw new RuntimeException("Failed to look up JNDI environment variable: " + name, ex);
		}
	}
}
