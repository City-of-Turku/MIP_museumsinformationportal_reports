package fi.turku.mip.reportserver.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Model class representing a Report Request
 *
 * @author mip
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportRequest {

	private Long id;
	private String owner;
	private String ownerEmail;
	private Date created;
	private Date modified;
	private String outputFile;
	private String requestedOutputType;

	private String statusMessage;
	private Report report;
	private ReportRequestStatus status;
	private List<ReportRequestParameter> parameters;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getOwnerEmail() {
		return ownerEmail;
	}
	public void setOwnerEmail(String ownerEmail) {
		this.ownerEmail = ownerEmail;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getModified() {
		return modified;
	}
	public void setModified(Date modified) {
		this.modified = modified;
	}
	public Report getReport() {
		return report;
	}
	public void setReport(Report report) {
		this.report = report;
	}
	public ReportRequestStatus getStatus() {
		return status;
	}
	public void setStatus(ReportRequestStatus status) {
		this.status = status;
	}
	public List<ReportRequestParameter> getParameters() {
		return parameters;
	}
	public void setParameters(List<ReportRequestParameter> parameters) {
		this.parameters = parameters;
	}
	public String getOutputFile() {
		return outputFile;
	}
	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}
	public String getStatusMessage() {
		return statusMessage;
	}
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
	public String getRequestedOutputType() {
		return requestedOutputType;
	}
	public void setRequestedOutputType(String requestedOutputType) {
		this.requestedOutputType = requestedOutputType;
	}


}
