package by.aab.jjb.m4a4.service;

import java.util.logging.Level;
import java.util.logging.Logger;

public class OrphanAccountException extends Exception {
	
	private static final String DEFAULT_MESSAGE = "Обнаружен счёт без владельца";

	public OrphanAccountException() {
		this(DEFAULT_MESSAGE);
	}
	
	OrphanAccountException(Throwable cause) {
		this(DEFAULT_MESSAGE, cause);
	}
	
	public OrphanAccountException(String message) {
		super(message);
		Logger.getGlobal().log(Level.WARNING, message);
	}
	
	OrphanAccountException(String message, Throwable cause) {
		super(message, cause);
		Logger.getGlobal().log(Level.WARNING, message);
	}

	private static final long serialVersionUID = -9222106789545066634L;

}
