package t.sql.exception;
/**
 * @author zhangj
 * @date   2018-05-20 09:38:25 
 * @email  zhangjin0908@hotmail.com
 */
public class TSQLException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public TSQLException() {
		super();
	}

	public TSQLException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public TSQLException(String message, Throwable cause) {
		super(message, cause);
	}

	public TSQLException(String message) {
		super(message);
	}

	public TSQLException(Throwable cause) {
		super(cause);
	}
	
}
