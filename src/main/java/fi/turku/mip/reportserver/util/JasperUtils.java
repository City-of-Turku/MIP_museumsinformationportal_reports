package fi.turku.mip.reportserver.util;

import java.io.File;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.FileResolver;
import net.sf.jasperreports.engine.util.JRElementsVisitor;
import net.sf.jasperreports.engine.util.JRLoader;

public class JasperUtils {

	private JasperReport compileReport(String reportPath, String reportSourceFileName) throws JRException {
		
		String compiledSourceFile;
		String sourceFile;
		
		if (reportSourceFileName.contains(".jasper")) {
			compiledSourceFile = reportSourceFileName;
			sourceFile = reportSourceFileName.replaceAll(".jasper", ".jrxml");
		} else {
			compiledSourceFile = reportSourceFileName.replaceAll(".jrxml", ".jasper");
			sourceFile = reportSourceFileName;			
		}
				
		File dst = new File(reportPath, compiledSourceFile);
		File src = new File(reportPath, sourceFile);
		
		JasperReport report = null;
		
		if (!dst.exists() || (src.exists() && (dst.lastModified() < src.lastModified()))) {
			JasperCompileManager.compileReportToFile(src.getAbsolutePath(), dst.getAbsolutePath());
		}
		
		report = (JasperReport)JRLoader.loadObjectFromFile(dst.getAbsolutePath());		
		// compile all subreports (if needed)
		JRElementsVisitor.visitReport(report, new SubReportVisitor(reportPath));
		
		return report;

	}

	private FileResolver getFileResolver(String path) {
		ParentPathFileResolver ppfr = new ParentPathFileResolver(path);
		return ppfr;
	}
	
}
