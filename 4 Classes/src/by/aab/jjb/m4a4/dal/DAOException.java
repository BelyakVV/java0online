package by.aab.jjb.m4a4.dal;

public class DAOException extends Exception {
	
	private static final long serialVersionUID = 821650963814359694L;

	public DAOException() {
		super();
	}

	public DAOException(Exception e) {
		super(e);
	}

	public DAOException(String message) {
		super(message);
	}

	public DAOException(Exception e, String message) {
		super(message, e);
	}
	
}
