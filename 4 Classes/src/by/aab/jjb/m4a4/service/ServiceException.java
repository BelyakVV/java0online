package by.aab.jjb.m4a4.service;

public class ServiceException extends Exception {

	public ServiceException() {
	}
	
	ServiceException(Throwable cause) {
		super(cause);
	}
	
	ServiceException(String message) {
		super(message);
	}
	
	ServiceException(String message, Throwable cause) {
		super(message, cause);
	}
	
	private static final long serialVersionUID = -5639696563814726885L;

}
