package fi.turku.mip.reportserver.config;

public class ReportServerConfiguration {

	private String reportSourceBasePath;
	private String reportOutputPath;

	public String getReportSourceBasePath() {
		return reportSourceBasePath;
	}

	public void setReportSourceBasePath(String reportSourceBasePath) {
		this.reportSourceBasePath = reportSourceBasePath;
	}

	public String getReportOutputPath() {
		return reportOutputPath;
	}

	public void setReportOutputPath(String reportOutputPath) {
		this.reportOutputPath = reportOutputPath;
	}
}
