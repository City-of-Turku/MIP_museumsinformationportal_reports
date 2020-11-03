package fi.turku.mip.reportserver.core;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

/**
 * Just a small helper to get the datasource by jndi lookup
 * @author mip
 *
 */
public class ReportDataSourceProvider {

	public static DataSource getDataSourceByJndiName(String jndiName) {
        final JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
        return dsLookup.getDataSource(jndiName);
	}

}
