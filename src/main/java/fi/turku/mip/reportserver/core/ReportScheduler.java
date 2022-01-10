package fi.turku.mip.reportserver.core;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import fi.turku.mip.reportserver.config.ReportServerConfiguration;
import fi.turku.mip.reportserver.model.ReportRequest;
import fi.turku.mip.reportserver.service.ReportRequestService;

/**
 * Scheduler checks queued and new report requests from database every 60 seconds and schedules them as tasks to the thread pool executor.
 * If the thread pool executor is "full", then the ReportTaskRejectedHandler will set the report request as queued.
 *
 * @author mip
 *
 */
@Component
public class ReportScheduler {

	private static final Logger logger = LogManager.getLogger(ReportScheduler.class);

	@Autowired
	private ThreadPoolTaskExecutor reportTaskExecutor;

	@Autowired
	private ReportRequestService reportRequestService;

	@Autowired
	private ReportServerConfiguration reportServerConfiguration;

	@Scheduled(fixedDelay=60000)
	public void scheduledRun() {

		try {
			// get the possible queued report requests from database
			logger.debug("Getting queued reportrequests from db");

			List<ReportRequest> queuedReportRequests = reportRequestService.getReportRequestsByStatus(1l);
			schedule(queuedReportRequests);

			logger.debug("Getting new reportrequests from db");

			// get the possible new report requests from database
			List<ReportRequest> newReportRequests = reportRequestService.getReportRequestsByStatus(0l);
			schedule(newReportRequests);

		} catch(Exception ex) {
			logger.error("Failure: " + ex.getMessage(), ex);
		}
	}

	private void schedule(List<ReportRequest> reportRequests) {
		if (reportRequests==null || reportRequests.isEmpty()) {
			logger.debug("No reportRequests found");
			return;
		}

		logger.debug("Found "+reportRequests.size()+" reportRequests.");

		for (ReportRequest reportRequest : reportRequests) {
			schedule(reportRequest);
		}
	}

	private void schedule(ReportRequest reportRequest) {
		// create a task to be run by the executor

		ReportTask rrt = new JasperReportTask();
		rrt.setReportRequest(reportRequest);
		rrt.setReportRequestService(reportRequestService);
		rrt.setReportServerConfiguration(reportServerConfiguration);

		// and schedule it to be run

		reportTaskExecutor.execute(rrt);
	}

}
