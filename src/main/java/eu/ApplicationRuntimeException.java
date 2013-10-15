package eu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ApplicationRuntimeException() {
		super();
	}

	public ApplicationRuntimeException(String message) {
		this(message, (String) null);

	}

	public ApplicationRuntimeException(String message, String description) {
		super(message);
		this.description = description;
	}

	public ApplicationRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApplicationRuntimeException(Throwable cause) {
		super(cause);
	}

	public int getExceptionCode() {
		return exceptionCode;
	}

	public String getDescription() {
		return description;
	}

	private int exceptionCode = 0;
	private String description;
	public static Logger logger =
			LoggerFactory.getLogger(ApplicationRuntimeException.class);
}
