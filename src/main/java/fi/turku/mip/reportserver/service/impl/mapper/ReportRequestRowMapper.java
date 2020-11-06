package fi.turku.mip.reportserver.service.impl.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import fi.turku.mip.reportserver.model.ReportRequest;

/**
 * RowMapper for report request
 *
 * @author mip
 *
 */
public class ReportRequestRowMapper extends BaseRowMapper implements RowMapper<ReportRequest> {

	private ReportRequestStatusRowMapper rrsrm = new ReportRequestStatusRowMapper();
	private ReportRowMapper rrm = new ReportRowMapper();

	public ReportRequest mapRow(ResultSet rs, int rowNum) throws SQLException {
		ReportRequest rr = new ReportRequest();

		rr.setId(getLong(rs, "rr_id"));
		rr.setOwner(rs.getString("rr_owner"));
		rr.setOwnerEmail(rs.getString("rr_owner_email"));
		rr.setCreated(rs.getTimestamp("rr_created"));
		rr.setModified(rs.getTimestamp("rr_modified"));
		rr.setOutputFile(rs.getString("rr_output_file"));
		rr.setStatusMessage(rs.getString("rr_status_message"));
		rr.setRequestedOutputType(rs.getString("rr_requested_output_type"));

		rr.setStatus(rrsrm.mapRow(rs, rowNum));
		rr.setReport(rrm.mapRow(rs, rowNum));

		return rr;
	}
}
