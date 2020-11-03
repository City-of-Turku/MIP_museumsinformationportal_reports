package fi.turku.mip.reportserver.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fi.turku.mip.reportserver.model.Report;
import fi.turku.mip.reportserver.model.ReportRequest;
import fi.turku.mip.reportserver.model.ReportRequestStatus;
import fi.turku.mip.reportserver.service.ReportRequestService;
import fi.turku.mip.reportserver.service.ReportService;
import fi.turku.mip.reportserver.util.MessageDataResponse;

/**
 * REST Controller to handle requests related to report requests
 *
 * @author mip
 *
 */
@RestController
public class ReportRequestController {

	@Autowired
	private ReportService reportService;

	@Autowired
	private ReportRequestService reportRequestService;

	/**
	 * REST service for getting report requests owned by given owner
	 *
	 * @param owner the owner
	 * @return a messageresponse with list of report requests as data
	 */
	@CrossOrigin
	@RequestMapping(path = "/reportRequests/{owner}", method = RequestMethod.GET)
	public MessageDataResponse getsRequestsByOwner(@PathVariable String owner) {
		return responseOk(reportRequestService.getReportRequestsByOwner(owner));
	}

	/**
	 * REST service for creating new report request
	 *
	 * @param reportRequest the report request to create
	 * @return a messageresponse with newly created report request as data if success, otherwise messageresponse with message as data
	 */
	@CrossOrigin
	@RequestMapping(path = "/reportRequest", method = RequestMethod.POST)
	public MessageDataResponse create(@RequestBody ReportRequest reportRequest) {

		// if the client did not know the id of the report, but supplied its name,
		// then find it by name and set to the request
		if (reportRequest.getReport().getId()==null) {
			Report r = reportService.getReportByName(reportRequest.getReport().getName());
			if (r==null) {
				return responseFail("Unknown report");
			}
			reportRequest.setReport(r);
		} else {
			// check that the report exists
			Report r = reportService.getReportById(reportRequest.getReport().getId());
			if (r==null) {
				return responseFail("Unknown report");
			}
		}

		return responseOk(reportRequestService.createReportRequest(reportRequest));
	}

	/**
	 * REST service for getting single report request
	 *
	 * @param reportRequestId the id of the report request
	 * @return a messageresponse with report request as data (or empty data if no such report request was found)
	 */
	@CrossOrigin
	@RequestMapping(path = "/reportRequest/{reportRequestId}", method = RequestMethod.GET)
	public MessageDataResponse get(@PathVariable Long reportRequestId) {
		return responseOk(reportRequestService.getReportRequest(reportRequestId));
	}

	/**
	 * REST service for deleting single report request (mark as deleted)
	 *
	 * @param reportRequestId the id of the report request
	 * @return a messageresponse with report request as data (or empty data if no such report request was found)
	 */
	@CrossOrigin
	@RequestMapping(path = "/reportRequest/{reportRequestId}", method = RequestMethod.DELETE)
	public MessageDataResponse delete(@PathVariable Long reportRequestId) {
		return responseOk(reportRequestService.deleteReportRequest(reportRequestId));
	}

	/**
	 * Service for downloading the actual created report file.
	 * Returns SC_NOT_FOUND when there is no such report request or the file itself is not found.
	 * Returns SC_BAD_REQUEST when the status of the report request is not SUCCESS.
	 *
	 * @param response the HttpServletResponse
	 * @param reportRequestId the id of the report request
	 * @throws IOException when reading the file fails
	 */
	@CrossOrigin
	@RequestMapping(path = "/reportRequest/{reportRequestId}/download", method = RequestMethod.GET)
	public void download(HttpServletResponse response, @PathVariable Long reportRequestId) throws IOException {

		// check that the report request exists..
		ReportRequest rr = reportRequestService.getReportRequest(reportRequestId);
		if (rr==null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		// ..and it is SUCCESS state
		if(!ReportRequestStatus.SUCCESS.equals(rr.getStatus().getId())) {
			System.out.println("invalid status? " + rr.getStatus().getId());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		File f = new File(rr.getOutputFile());

		// check that the actual file also exists
		if (!f.exists()) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		// try to guess the mime type..
		String mimeType = URLConnection.guessContentTypeFromName(f.getName());
        if(mimeType==null) {
            mimeType = "application/octet-stream";
        }
        // set some headers
        response.setContentType(mimeType);
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", f.getName()));
        response.setContentLength((int)f.length());

        InputStream inputStream = new BufferedInputStream(new FileInputStream(f));
        // copy the stream and close the streams
        FileCopyUtils.copy(inputStream, response.getOutputStream());
        // done
	}

	/*
	 * create OK response with given data
	 */
	private MessageDataResponse responseOk(Object data) {
		return new MessageDataResponse("OK", data);
	}

	/*
	 * create FAIL response with given data
	 */
	private MessageDataResponse responseFail(Object data) {
		return new MessageDataResponse("FAIL", data);
	}

}
