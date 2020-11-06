package fi.turku.mip.reportserver.service;

import java.util.List;

import fi.turku.mip.reportserver.model.ReportRequest;
import fi.turku.mip.reportserver.model.ReportRequestParameter;
import fi.turku.mip.reportserver.model.ReportRequestStatus;

/**
 * Service for report request operations
 *
 * @author mip
 *
 */
public interface ReportRequestService {

	/**
	 * Create a new report request
	 *
	 * @param reportRequest the report request to create
	 * @return reportRequest with all fields populated (id, timestamps)
	 */
	public ReportRequest createReportRequest(ReportRequest reportRequest);

	/**
	 * Get a report request identified with given id
	 *
	 * @param id the id of the report request to get
	 * @return the report request or null if none exists
	 */
	public ReportRequest getReportRequest(Long id);

	/**
	 * Delete a report request identified with given id
	 *
	 * @param id the id of the report request to delete
	 * @return the report request or null if none exists
	 */
	public Object deleteReportRequest(Long reportRequestId);

	/**
	 * Get the status of requested report request
	 *
	 * @param reportRequestId the id of the report request to get status for
	 * @return report requests status
	 */
	public ReportRequestStatus getReportRequestStatus(Long reportRequestId);

	/**
	 * Get a list of report requests of given owner
	 *
	 * @param owner the owner whose report requests to get
	 * @return a list of report requests or an empty list if none found
	 */
	public List<ReportRequest> getReportRequestsByOwner(String owner);

	/**
	 * Get a list of report requests in given state
	 *
	 * @param statusId the id of status
	 * @return a list of report requests or an empty list if none found
	 */
	public List<ReportRequest> getReportRequestsByStatus(Long statusId);

	/**
	 * Set the status of a report request
	 *
	 * @param reportRequestId the id of the report request whose status is to be changed
	 * @param statusId the id of the status to set
	 */
	public void setReportRequestStatus(Long reportRequestId, Long statusId);

	/**
	 * Set the output file location of the report request
	 *
	 * @param reportRequestId the id of the report request
	 * @param outputFileName the filename to set
	 */
	public void setReportRequestOutputFile(Long reportRequestId, String outputFileName);

	/**
	 * Get global parameters for report requests.
	 * Global parameters are used for every report creation
	 *
	 * @return a list of parameters or an empty list if none found
	 */
	public List<ReportRequestParameter> getGlobalParameters();

}
