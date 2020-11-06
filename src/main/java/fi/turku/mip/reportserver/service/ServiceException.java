package fi.turku.mip.reportserver.service;

/**
 * Service exception to be used on service failures
 *
 * @author mip
 *
 */
public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = 429499078822885679L;

	public ServiceException() {
		super();
	}

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
