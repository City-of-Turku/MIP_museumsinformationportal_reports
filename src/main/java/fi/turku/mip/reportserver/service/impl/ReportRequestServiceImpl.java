package fi.turku.mip.reportserver.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import fi.turku.mip.reportserver.model.ReportRequest;
import fi.turku.mip.reportserver.model.ReportRequestParameter;
import fi.turku.mip.reportserver.model.ReportRequestStatus;
import fi.turku.mip.reportserver.service.ReportRequestService;
import fi.turku.mip.reportserver.service.ServiceException;
import fi.turku.mip.reportserver.service.impl.mapper.ReportRequestParameterRowMapper;
import fi.turku.mip.reportserver.service.impl.mapper.ReportRequestRowMapper;
import fi.turku.mip.reportserver.service.impl.mapper.ReportRequestStatusRowMapper;

/**
 * Report request service implementation
 *
 * @author mip
 *
 */
@Service
public class ReportRequestServiceImpl extends BaseServiceImpl implements ReportRequestService {

	/*
	 * (non-Javadoc)
	 * @see fi.turku.mip.reportserver.service.ReportRequestService#createReportRequest(fi.turku.mip.reportserver.model.ReportRequest)
	 */
	public ReportRequest createReportRequest(final ReportRequest reportRequest) {

		Long newId = transactionTemplate.execute(new TransactionCallback<Long>() {
			public Long doInTransaction(TransactionStatus transactionStatus) {
				try {

					KeyHolder keyHolder = new GeneratedKeyHolder();

					jdbcTemplate.update(new PreparedStatementCreator() {
						public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
							PreparedStatement ps = con.prepareStatement(getQuery("CREATE_REPORT_REQUEST"), new String[] { "id" });
							ps.setLong(1, reportRequest.getReport().getId());
							ps.setLong(2, ReportRequestStatus.CREATED);
							ps.setString(3, reportRequest.getOwner());
							ps.setString(4,  reportRequest.getOwnerEmail());
							ps.setString(5, reportRequest.getRequestedOutputType());
							return ps;
						}
					}, keyHolder);

					Long reportRequestId = keyHolder.getKey().longValue();

					for (ReportRequestParameter parameter: reportRequest.getParameters()) {
						Object[] param_args = new Object[] {
							reportRequestId,
							parameter.getName(),
							parameter.getValue()
						};
						jdbcTemplate.update(getQuery("CREATE_REPORT_REQUEST_PARAMETER"), param_args);
					}

					return reportRequestId;

				} catch (Exception e) {
					transactionStatus.setRollbackOnly();
					throw new ServiceException(e.getMessage(), e);
				}
			}
		});

		return getReportRequest(newId);
	}

	/*
	 * (non-Javadoc)
	 * @see fi.turku.mip.reportserver.service.ReportRequestService#getReportRequest(java.lang.Long)
	 */
	public ReportRequest getReportRequest(Long id) {
		Object[] args = new Object[] {
			id
		};
		ReportRequest rr = null;

		try {
			rr = jdbcTemplate.queryForObject(getQuery("GET_REPORT_REQUEST"), args, new ReportRequestRowMapper());
		} catch(EmptyResultDataAccessException erdae) {
			// ignore this exception, just return null when no report request was found
			return null;
		}

		if (rr.getId()!=null) {
			rr.setParameters(getReportRequestParameters(rr.getId()));
		}

		return rr;
	}

	/*
	 * (non-Javadoc)
	 * @see fi.turku.mip.reportserver.service.ReportRequestService#getReportRequest(java.lang.Long)
	 */
	public Object deleteReportRequest(Long id) {
		Object[] args = new Object[] {
				id
		};
		int affectedRows = 0;

		try {
			affectedRows = jdbcTemplate.update(getQuery("DELETE_REPORT_REQUEST"), args);
		} catch(EmptyResultDataAccessException erdae) {
			// ignore this exception, just return null when no report request was found
			return null;
		}

		//TODO: If affectedRows != 1, throw error

		return affectedRows;
	}

	/**
	 * Get report request parameters
	 *
	 */
	private List<ReportRequestParameter> getReportRequestParameters(Long reportRequestId) {
		Object[] args = new Object[] {
			reportRequestId
		};
		return jdbcTemplate.query(getQuery("GET_REPORT_REQUEST_PARAMETERS"), args, new ReportRequestParameterRowMapper());
	}

	/*
	 * (non-Javadoc)
	 * @see fi.turku.mip.reportserver.service.ReportRequestService#getReportRequestStatus(java.lang.Long)
	 */
	public ReportRequestStatus getReportRequestStatus(Long reportRequestId) {
		Object[] args = new Object[] {
			reportRequestId
		};
		return jdbcTemplate.queryForObject(getQuery("GET_REPORT_REQUEST_STATUS"), args, new ReportRequestStatusRowMapper());
	}

	/*
	 * (non-Javadoc)
	 * @see fi.turku.mip.reportserver.service.ReportRequestService#getReportRequestsByOwner(java.lang.String)
	 */
	public List<ReportRequest> getReportRequestsByOwner(String owner) {
		Object[] args = new Object[] {
			owner
		};
		List<ReportRequest> rrs = jdbcTemplate.query(getQuery("GET_REPORT_REQUESTS_BY_ONWER"), args, new ReportRequestRowMapper());

		for (ReportRequest reportRequest : rrs) {
			reportRequest.setParameters(getReportRequestParameters(reportRequest.getId()));
		}

		return rrs;
	}

	/*
	 * (non-Javadoc)
	 * @see fi.turku.mip.reportserver.service.ReportRequestService#getReportRequestsByStatus(java.lang.Long)
	 */
	public List<ReportRequest> getReportRequestsByStatus(Long statusId) {
		Object[] args = new Object[] {
			statusId
		};
		List<ReportRequest> rrs = jdbcTemplate.query(getQuery("GET_REPORT_REQUESTS_BY_STATUS"), args, new ReportRequestRowMapper());

		for (ReportRequest reportRequest : rrs) {
			reportRequest.setParameters(getReportRequestParameters(reportRequest.getId()));
		}

		return rrs;
	}

	/*
	 * (non-Javadoc)
	 * @see fi.turku.mip.reportserver.service.ReportRequestService#setReportRequestStatus(java.lang.Long, java.lang.Long)
	 */
	public void setReportRequestStatus(Long reportRequestId, Long statusId) {
		Object[] args = new Object[] {
				statusId, reportRequestId
		};

		jdbcTemplate.update(getQuery("SET_REPORT_REQUEST_STATUS"), args);
	}

	/*
	 * (non-Javadoc)
	 * @see fi.turku.mip.reportserver.service.ReportRequestService#setReportRequestOutputFile(java.lang.Long, java.lang.String)
	 */
	public void setReportRequestOutputFile(Long reportRequestId, String outputFileName) {
		Object[] args = new Object[] {
				outputFileName, reportRequestId
		};

		jdbcTemplate.update(getQuery("SET_REPORT_REQUEST_OUTPUT_FILE"), args);
	}

	/*
	 * (non-Javadoc)
	 * @see fi.turku.mip.reportserver.service.ReportRequestService#getGlobalParameters()
	 */
	public List<ReportRequestParameter> getGlobalParameters() {
		return jdbcTemplate.query(getQuery("GET_ACTIVE_GLOBAL_PARAMETERS"), new ReportRequestParameterRowMapper());
	}

}
