package com.fixiu.jdbc.exception;

/**
 * @author dongyushuai
 */
public class PageException extends RuntimeException {

    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 4035235693203426925L;

	public PageException(String message) {
        super(message);
    }

    public PageException(String message, Throwable cause) {
        super(message, cause);
    }
}
