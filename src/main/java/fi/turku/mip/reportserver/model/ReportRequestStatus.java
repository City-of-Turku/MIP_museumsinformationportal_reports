package fi.turku.mip.reportserver.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Model object representing a Report Request Status
 *
 * @author mip
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportRequestStatus {

	public static final Long CREATED = 0l;
	public static final Long QUEUED = 1l;
	public static final Long RUNNING = 2l;
	public static final Long SUCCESS = 3l;
	public static final Long FAILED = 4l;

	private Long id;
	private String name;

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


}
