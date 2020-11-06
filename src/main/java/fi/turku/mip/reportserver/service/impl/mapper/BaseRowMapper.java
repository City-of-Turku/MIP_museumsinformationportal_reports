package fi.turku.mip.reportserver.service.impl.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * RowMapper base class than contains useful utility methods.
 * (Note: should be some util class with static methods?)
 *
 * @author mip
 *
 */
public class BaseRowMapper {

	/**
	 * Get a Long from database, returns null if the value was null
	 *
	 * @param rs the resultset to read
	 * @param columnName the column name to read
	 * @return the Long value or null if it was indeed null in database
	 * @throws SQLException when fails
	 */
	public Long getLong(ResultSet rs, String columnName) throws SQLException {
		Long l = rs.getLong(columnName);
		if (rs.wasNull()) {
			return null;
		}
		return l;
	}
}
