package fi.turku.mip.reportserver.core;

import java.io.File;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jndi.JndiTemplate;

import fi.turku.mip.reportserver.model.Report;
import fi.turku.mip.reportserver.model.ReportRequest;
import fi.turku.mip.reportserver.model.ReportRequestParameter;
import fi.turku.mip.reportserver.util.ParentPathFileResolver;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.FileResolver;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

/**
 * JasperReportTask is for running jasper report creations.
 *
 * @author mip
 *
 */
public class JasperReportTask extends ReportTask {

	// the logger
	private static final Logger logger = Logger.getLogger(JasperReportTask.class);

	// date formats used when parsing date type parameters
	private static final String DATE_FORMAT_PATTERN = "dd.MM.yyyy";
	private static final String LONG_DATE_FORMAT_PATTERN = "dd.MM.yyyy HH:mm:ss";

	@Override
	public File makeReport() {

		logger.info("Start making Jasper report, report request id: " + reportRequest.getId());

		Connection con = null;

		try {

			Report report = reportRequest.getReport();
			File reportSourcePath = new File(reportServerConfiguration.getReportSourceBasePath(), report.getReportSourcePath());

			File reportSource = new File(reportSourcePath, report.getReportSourceFile());

			JasperReport jasperReport = (JasperReport)JRLoader.loadObjectFromFile(reportSource.getAbsolutePath());

			Map<String, Object> parameters = prepareParameters(jasperReport, reportRequest);

			DataSource ds = ReportDataSourceProvider.getDataSourceByJndiName(report.getDataSourceJndiName());
			if (ds==null) {
				throw new RuntimeException("DataSource " + report.getDataSourceJndiName() + " not found. Misconfiguration?");
			}
			// get the connection for report data
			con = ds.getConnection();

			// fill the report
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, con);

			// export it to requested output type
			if ("PDF".equalsIgnoreCase(reportRequest.getRequestedOutputType())) {

				File outputFile = getOutputFile(reportRequest, ".pdf");
				JasperExportManager.exportReportToPdfFile(jasperPrint, outputFile.getAbsolutePath());
				return outputFile;

			} else if ("WORD".equalsIgnoreCase(reportRequest.getRequestedOutputType())) {

				Exporter exporter = new JRDocxExporter();
				exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

				File outputFile = getOutputFile(reportRequest, ".docx");
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputFile));

				exporter.exportReport();
				return outputFile;

			} else if ("EXCEL".equalsIgnoreCase(reportRequest.getRequestedOutputType())) {

				Exporter exporter = new JRXlsxExporter();
				exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

				File outputFile = getOutputFile(reportRequest, ".xlsx");
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputFile));

				exporter.exportReport();
				return outputFile;

			} else if ("HTML".equalsIgnoreCase(reportRequest.getRequestedOutputType())) {

				File outputFile = getOutputFile(reportRequest, ".html");
				JasperExportManager.exportReportToHtmlFile(jasperPrint, outputFile.getAbsolutePath());
				return outputFile;

			} else {
				throw new Exception("Unknown output file type requested: " + reportRequest.getRequestedOutputType());
			}

		} catch(Exception e) {
			// just throw it
			throw new RuntimeException(e);
		} finally {
			try {
				if (con!=null) {
					con.close();
				}
			} catch(Exception ignorex) {
				// ignore exceptions on connection close
			}
		}

	}

	/**
	 * Create a map of parameters for jasper report creation.
	 * This method checks from jasper report the java class of the parameter and then tries to
	 * convert the parameter from database (which is always string) to the correct class.
	 * The global parameters are also added, and the file resolver that finds the related files.
	 * If such a parameter is found which can not be handled (not implemented) a RuntimeException is thrown.
	 *
	 * Note that global parameters are not type converted, they will always be added as strings.
	 * Note that global parameters are not checked that they exists in the report, they are always added.
	 *
	 * @param report the jasperReport object
	 * @param reportRequest the report request object
	 * @return a map containing parameters
	 */
	private Map<String, Object> prepareParameters(JasperReport report, ReportRequest reportRequest) {
		Map<String, Object> reportParameters = new HashMap<String, Object>();

		// put all request parameters to the map
		for (ReportRequestParameter parameter: reportRequest.getParameters()) {

			// looping through the parameters and trying to figure out what types of parameters these are,
			// and then creating the parameters as their correct types
			JRParameter[] params = report.getParameters();
			for (JRParameter jrParameter : params) {
				if (!jrParameter.isSystemDefined() && jrParameter.getName().equals(parameter.getName())) {

					if (jrParameter.getValueClass().isAssignableFrom(java.lang.String.class)) {

						reportParameters.put(parameter.getName(), parameter.getValue());
					} else if (jrParameter.getValueClass().isAssignableFrom(java.lang.Long.class)) {

						Long l = Long.valueOf(parameter.getValue());
						reportParameters.put(parameter.getName(), l);
					} else if (jrParameter.getValueClass().isAssignableFrom(java.lang.Integer.class)) {

						Integer i = Integer.valueOf(parameter.getValue());
						reportParameters.put(parameter.getName(), i);
					} else if (jrParameter.getValueClass().isAssignableFrom(java.lang.Double.class)) {

						Double d = Double.valueOf(parameter.getValue());
						reportParameters.put(parameter.getName(), d);
					} else if (jrParameter.getValueClass().isAssignableFrom(Date.class)) {

						try {
							SimpleDateFormat sdf = new SimpleDateFormat(LONG_DATE_FORMAT_PATTERN);
							Date d = sdf.parse(parameter.getValue());
							reportParameters.put(parameter.getName(), d);
						} catch(ParseException pe) {
							logger.debug("Could not parse date "+parameter.getValue()+" with long pattern, trying short.");
							try {
								SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_PATTERN);
								Date d = sdf.parse(parameter.getValue());
								reportParameters.put(parameter.getName(), d);
							} catch(ParseException pex) {
								throw new RuntimeException("Unparseable date: "+parameter.getValue()+": " + pex.getMessage(), pex);
							}
						}
					} else {
						throw new RuntimeException("Unsupported parameter value class:" + jrParameter.getValueClassName()+" for parameter: "+jrParameter.getName()
							+". This is ReportServer problem, not jasper problem, please implement correct handling for this parameter value class.");
					}
				}
			}
		}

		// get all "global" parameters and put them to the jasper parameters
		reportParameters.putAll(getGlobalParameters());

		// file resolver to find all related files in the same folder (subreports, images)
		File reportSourcePath = new File(reportServerConfiguration.getReportSourceBasePath(), reportRequest.getReport().getReportSourcePath());
		reportParameters.put("REPORT_FILE_RESOLVER", getFileResolver(reportSourcePath.getAbsolutePath()));

		return reportParameters;
	}

	/**
	 * Get the global parameters from database
	 *
	 * @return a map containing all global parameters
	 */
	private Map<String, Object> getGlobalParameters() {
		Map<String, Object> globalParameters = new HashMap<String, Object>();

		if (globalReportRequestParameters==null || globalReportRequestParameters.isEmpty()) {
			return globalParameters;
		}

		for (ReportRequestParameter reportRequestParameter : globalReportRequestParameters) {
			globalParameters.put(reportRequestParameter.getName(), reportRequestParameter.getValue());
		}

		return globalParameters;
	}

	private File getOutputFile(ReportRequest reportRequest, String extension) {

		String reportName = "report-"; //Default file name
		String reportType = reportRequest.getReport().getName(); //Report name
		if(reportType.length() > 0) { //If we have valid report name, use that instead.
			reportName = reportType;
		}

		return new File(reportServerConfiguration.getReportOutputPath(), reportName + "-" + reportRequest.getId() + extension);
	}

	/**
	 * Get a file resolver for jasper engine to find all report related files (subreports, images, etc)
	 * from given path.
	 *
	 * @param path the path for the report files
	 * @return a file resolver
	 */
	private FileResolver getFileResolver(String path) {
		ParentPathFileResolver ppfr = new ParentPathFileResolver(path);
		return ppfr;
	}
}
