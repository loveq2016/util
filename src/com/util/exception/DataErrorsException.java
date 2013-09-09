package com.util.exception;

public class DataErrorsException  extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4806442504034958623L;

	public DataErrorsException() {
		super();
	}

	public DataErrorsException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataErrorsException(String message) {
		super(message);
	}
	 
	public DataErrorsException(Throwable cause) {
		super(cause);
	}
	
}
