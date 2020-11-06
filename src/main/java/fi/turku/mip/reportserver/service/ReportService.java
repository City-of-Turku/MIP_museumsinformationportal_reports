package fi.turku.mip.reportserver.service;

import fi.turku.mip.reportserver.model.Report;

/**
 * Service interface for report related operations
 *
 * @author mip
 *
 */
public interface ReportService {

	/**
	 * Get report by name
	 *
	 * @param name the report name
	 * @return the report object or null if none found
	 */
	public Report getReportByName(String name);

	/**
	 * Get report by id
	 *
	 * @param id the report id
	 * @return the report object or null if none found
	 */
	public Report getReportById(Long id);

}
