package fi.turku.mip.reportserver.util;

import java.io.File;

import net.sf.jasperreports.engine.util.FileResolver;

/**
 * Jasper utility class for finding the files that are related to a report (subreports, images etc)
 *
 * @author mip
 *
 */
public class ParentPathFileResolver implements FileResolver {

	private String parentPath;

	/**
	 * Constructor to create new file resolver
	 *
	 * @param parentPath the complete path where report files exists
	 */
	public ParentPathFileResolver(String parentPath) {
		this.parentPath = parentPath;
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.jasperreports.engine.util.FileResolver#resolveFile(java.lang.String)
	 */
	public File resolveFile(String fileName) {
		return new File(parentPath, fileName);
	}
}
