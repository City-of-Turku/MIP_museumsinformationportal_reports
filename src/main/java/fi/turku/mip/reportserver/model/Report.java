package fi.turku.mip.reportserver.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Model class representing a Report object
 *
 * @author mip
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Report {

	private Long id;
	private String name;
	private String description;
	private String reportSourcePath;
	private String reportSourceFile;
	private String dataSourceJndiName;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getReportSourceFile() {
		return reportSourceFile;
	}
	public void setReportSourceFile(String reportSourceFile) {
		this.reportSourceFile = reportSourceFile;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getReportSourcePath() {
		return reportSourcePath;
	}
	public void setReportSourcePath(String reportSourcePath) {
		this.reportSourcePath = reportSourcePath;
	}
	public String getDataSourceJndiName() {
		return dataSourceJndiName;
	}
	public void setDataSourceJndiName(String dataSourceJndiName) {
		this.dataSourceJndiName = dataSourceJndiName;
	}

}
