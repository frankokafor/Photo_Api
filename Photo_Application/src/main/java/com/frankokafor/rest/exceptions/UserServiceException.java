package com.frankokafor.rest.exceptions;

public class UserServiceException extends RuntimeException{
	private static final long serialVersionUID = 703824568294280463L;
	
	private String message;

	public UserServiceException(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	

}
