package fi.turku.mip.reportserver.service.impl.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import fi.turku.mip.reportserver.model.ReportRequestParameter;

/**
 * RowMapper for ReportRequestParameter
 *
 * @author mip
 *
 */
public class ReportRequestParameterRowMapper extends BaseRowMapper implements RowMapper<ReportRequestParameter> {

	public ReportRequestParameter mapRow(ResultSet rs, int rowNum) throws SQLException {

		ReportRequestParameter rrp = new ReportRequestParameter();

		rrp.setId(getLong(rs, "rrp_id"));
		rrp.setName(rs.getString("rrp_name"));
		rrp.setValue(rs.getString("rrp_value"));

		return rrp;
	}
}
