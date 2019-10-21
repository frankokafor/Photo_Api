package com.frankokafor.rest.model.response;

import java.util.Date;

public class CustomErrorMessage {
	private Date timestamp;
	private String message;

	public CustomErrorMessage() {
		super();
	}

	public CustomErrorMessage(Date timestamp, String message) {
		super();
		this.timestamp = timestamp;
		this.message = message;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
