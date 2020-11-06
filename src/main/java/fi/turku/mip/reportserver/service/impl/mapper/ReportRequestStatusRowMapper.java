package fi.turku.mip.reportserver.service.impl.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import fi.turku.mip.reportserver.model.ReportRequestStatus;

/**
 * RowMapper for report request status
 *
 * @author mip
 *
 */
public class ReportRequestStatusRowMapper extends BaseRowMapper implements RowMapper<ReportRequestStatus> {

	public ReportRequestStatus mapRow(ResultSet rs, int rowNum) throws SQLException {
		ReportRequestStatus rrs = new ReportRequestStatus();
		rrs.setId(getLong(rs, "rrs_id"));
		rrs.setName(rs.getString("rrs_name"));
		return rrs;
	}
}
