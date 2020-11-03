package fi.turku.mip.reportserver.service.impl.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import fi.turku.mip.reportserver.model.Report;

/**
 * RowMapper for report
 *
 * @author mip
 *
 */
public class ReportRowMapper extends BaseRowMapper implements RowMapper<Report> {

	public Report mapRow(ResultSet rs, int row) throws SQLException {
		Report r = new Report();

		r.setId(getLong(rs, "r_id"));
		r.setName(rs.getString("r_name"));
		r.setDescription(rs.getString("r_description"));
		r.setReportSourcePath(rs.getString("r_reportsource_path"));
		r.setReportSourceFile(rs.getString("r_reportsource_file"));
		r.setDataSourceJndiName(rs.getString("r_datasource_jndi_name"));

		return r;
	}
}
