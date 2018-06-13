package t.sql.exception;

public class TVerificationException  extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public TVerificationException() {
		super();
	}

	public TVerificationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public TVerificationException(String message, Throwable cause) {
		super(message, cause);
	}

	public TVerificationException(String message) {
		super(message);
	}

	public TVerificationException(Throwable cause) {
		super(cause);
	}
	
}
