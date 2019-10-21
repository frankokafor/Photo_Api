package com.frankokafor.rest.model.response;

public enum ErrorMessages {
	
	MISSING_REQUIRED_FIELD("missing required field. please check documentation for required field"),
	RECORD_ALREADY_EXISTS("the input record already exists"),
	INTERNAL_SERVER_ERROR("ineternal server error. please contact the administrator"),
	NO_RECORD_FOUND("the input record could not be found"),
	AUTHENTICATION_FAILED("user authentication failes"),
	COULD_NOT_UPDATE_RECORD("the input record could not be updated"),
	COULD_NOT_DELETE_RECORD("the input record could not be deleted"),
	EMAIL_ADDRESS_NOT_VERIFIED("email address could not be verified");
	
	
	
	private String errorMessages;

	ErrorMessages(String errorMessages) {
		this.errorMessages = errorMessages;
	}

	public String getErrorMessages() {
		return errorMessages;
	}

	public void setErrorMessages(String errorMessages) {
		this.errorMessages = errorMessages;
	}
	
	
}
