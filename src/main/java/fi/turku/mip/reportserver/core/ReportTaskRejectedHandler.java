package fi.turku.mip.reportserver.core;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.beans.factory.annotation.Autowired;

import fi.turku.mip.reportserver.model.ReportRequestStatus;
import fi.turku.mip.reportserver.service.ReportRequestService;

/**
 * Class to handle the case when report executor rejects the task.
 * This class sets the report requests status to queued when the task is rejected.
 *
 * @author mip
 *
 */
public class ReportTaskRejectedHandler implements RejectedExecutionHandler {

	@Autowired
	private ReportRequestService reportRequestService;

	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
		ReportTask rt = (ReportTask)r;

		Long reportRequestId = rt.getReportRequest().getId();
		reportRequestService.setReportRequestStatus(reportRequestId, ReportRequestStatus.QUEUED);
	}

}
