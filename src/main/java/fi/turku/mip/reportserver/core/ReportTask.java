package fi.turku.mip.reportserver.core;

import java.io.File;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import fi.turku.mip.reportserver.config.ReportServerConfiguration;
import fi.turku.mip.reportserver.model.ReportRequest;
import fi.turku.mip.reportserver.model.ReportRequestParameter;
import fi.turku.mip.reportserver.model.ReportRequestStatus;
import fi.turku.mip.reportserver.service.ReportRequestService;

/**
 * A report task for running report creations
 *
 * @author mip
 *
 */
public abstract class ReportTask implements Runnable {

	// the logger
	private static final Logger logger = LogManager.getLogger(ReportTask.class);
	// the report request
	protected ReportRequest reportRequest;
	// the global report request parameters
	protected List<ReportRequestParameter> globalReportRequestParameters;
	// service for handling report requests
	protected ReportRequestService reportRequestService;
	// the main configuration
	protected ReportServerConfiguration reportServerConfiguration;

	/**
	 * Run the report creation task
	 */
	public void run() {

		try {      
			logger.info("Start prosessing report request: " + reportRequest.getId());

			// get the global report request parameters
			globalReportRequestParameters = reportRequestService.getGlobalParameters();

			// set the report request as running
			reportRequestService.setReportRequestStatus(reportRequest.getId(), ReportRequestStatus.RUNNING);

			// MAKE THE REPORT
			File createdReport = makeReport();

			// set the output file to request
			reportRequestService.setReportRequestOutputFile(reportRequest.getId(), createdReport.getAbsolutePath());

			// set the report request success
			reportRequestService.setReportRequestStatus(reportRequest.getId(), ReportRequestStatus.SUCCESS);

			logger.info("Report creation of report request "+reportRequest.getId()+" successfully completed");

		} catch(Exception ex) {
			// report creation failed, log the cause and set report request to failed state
			logger.error("Report creation of report request "+reportRequest.getId()+" failed: " + ex.getMessage(), ex);
			// TODO: the possible cause to database? Not really useful to the end user...
			try {
				reportRequestService.setReportRequestStatus(reportRequest.getId(), ReportRequestStatus.FAILED);
			} catch(Exception stateEx) {
				logger.error("Setting report request "+reportRequest.getId()+" to FAILED state failed: " + stateEx.getMessage(), stateEx);
			}
		}
	}

	/**
	 * Make the actual report, return the created File
	 *
	 * @return the created File
	 */
	public abstract File makeReport();

	public ReportRequest getReportRequest() {
		return reportRequest;
	}

	public void setReportRequest(ReportRequest reportRequest) {
		this.reportRequest = reportRequest;
	}

	public ReportRequestService getReportRequestService() {
		return reportRequestService;
	}

	public void setReportRequestService(ReportRequestService reportRequestService) {
		this.reportRequestService = reportRequestService;
	}

	public ReportServerConfiguration getReportServerConfiguration() {
		return reportServerConfiguration;
	}

	public void setReportServerConfiguration(ReportServerConfiguration reportServerConfiguration) {
		this.reportServerConfiguration = reportServerConfiguration;
	}
}
