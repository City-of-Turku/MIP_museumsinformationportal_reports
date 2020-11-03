package fi.turku.mip.reportserver.util;

/**
 * REST response with message and data
 *
 * @author mip
 *
 */
public class MessageDataResponse {

	private String message;
	private Object data;

	public MessageDataResponse(String message, Object data) {
		this.message = message;
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}


}
