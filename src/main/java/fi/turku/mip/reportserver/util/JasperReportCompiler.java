package fi.turku.mip.reportserver.util;

import java.io.File;
import java.io.FilenameFilter;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;

/**
 * Quick and dirty jasper report compiler.
 *
 *
 * @author mip
 *
 */
public class JasperReportCompiler {

	File jrxmlPath = null;
	boolean recursive = false;

	/**
	 * Construct new jasper report compiler.
	 *
	 * @param jrxmlPath the path to search for .jrxml files
	 * @param recursive should the compiler recurse to sub directories when searching for jrxml files?
	 */
	public JasperReportCompiler(String jrxmlPath, boolean recursive) {
		this.recursive = recursive;
		File path = new File(jrxmlPath);

		if (!path.exists()) {
			s("Error: given path does not exists.");
			return;
		}
		if (!path.isDirectory()) {
			s("Error: given path is not a directory.");
			return;
		}
		this.jrxmlPath = path;
	}

	/*
	 * Compile
	 */
	private void compile() throws JRException {
		compileInternal(jrxmlPath);
	}

	/*
	 * Compile starting from given path path
	 */
	private void compileInternal(File path) throws JRException {

		// search for .jrxml files in given directory
		String[] jrxmlFiles = path.list(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".jrxml");
			}
		});

		// loop through the found .jrxml files and compile them (to same directory where the jrxml file is)
		for (String jrxmlFile : jrxmlFiles) {
			File jrxml = new File(path, jrxmlFile);

			// By default, the compiler creates the output file name from the
			// "name" attribute of jasperReport xml element in the jrxml.
			// That is not what we want, so make the compiled report
			// the same name as the source file.
			File jasper = new File(path, jrxmlFile.replaceAll(".jrxml", ".jasper"));

			s("Compiling: "+jrxml.getAbsolutePath() + " to " + jasper.getAbsolutePath());
			JasperCompileManager.compileReportToFile(jrxml.getAbsolutePath(), jasper.getAbsolutePath());
		}

		// if recursive search is requested, then drill down to subdirectories
		if (recursive) {
			for (File subdir : path.listFiles()) {
				if (subdir.isDirectory()) {
					compileInternal(subdir);
				}
			}
		}
	}

	/**
	 * Main
	 * Parameters
	 *  -r (optional) if compiler should recurse to subdirectories
	 *  -s <path> (mandatory) = to path from where to start searching for .jrxml files
	 */
	public static void main(String[] args) {
		// instantiate a new argument parser to parse the command like arguments
		ArgumentParser ap = new ArgumentParser(args);

		// validate required arguments
		if (!validateArguments(ap)) {
			printUsage();
			return;
		}

		// get the arguments to variables - just for clarity
		boolean recurse = ap.hasArgument("-r");
		String path = ap.getArgumentOptions("-s").get(0);

		// instantiate new compiler and create the jasper files
		JasperReportCompiler rc = new JasperReportCompiler(path, recurse);
		try {
			s("ReportCompiler Start.");
			rc.compile();
		} catch (JRException e) {
			s("Error while compiling: " + e.getMessage());
			e.printStackTrace();
		}
		s("ReportCompiler Done.");
	}

	private static boolean validateArguments(ArgumentParser ap) {
		if (!ap.hasArgument("-s")) {
			return false;
		}
	    if (ap.getArgumentOptions("-s").isEmpty()) {
	    	return false;
	    }
		return true;
	}

	private static void printUsage() {
		s("Usage:");
		s("ReportCompiler [-r] -s <path_to_jrxml>");
		s("  Where ");
		s("    -r = recurse subdirectories");
		s("    -s <path_to_jrxml> = directory to search for .jrxml files to compile");
	}

	private static String s(String s) {
		System.out.println(s);
		return s;
	}
}
