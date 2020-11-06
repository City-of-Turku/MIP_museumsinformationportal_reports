package fi.turku.mip.reportserver.service.impl;

import org.springframework.stereotype.Service;

import fi.turku.mip.reportserver.model.Report;
import fi.turku.mip.reportserver.service.ReportService;
import fi.turku.mip.reportserver.service.impl.mapper.ReportRowMapper;

/**
 * Report service implementation
 *
 * @author mip
 *
 */
@Service
public class ReportServiceImpl extends BaseServiceImpl implements ReportService {

	/*
	 * (non-Javadoc)
	 * @see fi.turku.mip.reportserver.service.ReportService#getReportByName(java.lang.String)
	 */
	public Report getReportByName(String name) {
		Object[] args = new Object[] {
				name
		};
		return jdbcTemplate.queryForObject(getQuery("GET_REPORT_BY_NAME"), args, new ReportRowMapper());
	}

	/*
	 * (non-Javadoc)
	 * @see fi.turku.mip.reportserver.service.ReportService#getReportById(java.lang.Long)
	 */
	public Report getReportById(Long id) {
		Object[] args = new Object[] {
				id
		};
		return jdbcTemplate.queryForObject(getQuery("GET_REPORT_BY_ID"), args, new ReportRowMapper());
	}

}
