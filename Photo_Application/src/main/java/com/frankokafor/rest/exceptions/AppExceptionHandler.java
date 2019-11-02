package com.frankokafor.rest.exceptions;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.frankokafor.rest.model.response.CustomErrorMessage;

@ControllerAdvice
public class AppExceptionHandler {

	@ExceptionHandler(value = { UserServiceException.class })
	public ResponseEntity<Object> userServiceExceptionHandler(UserServiceException ex, WebRequest request) {
		CustomErrorMessage errorMessage = new CustomErrorMessage(new Date(), ex.getMessage());
		return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
		// this class will handle a specific error called userService exception
	}
	
	@ExceptionHandler(value = {Exception.class })
	public ResponseEntity<Object> otherExceptionHandler(Exception ex, WebRequest request) {
		CustomErrorMessage errorMessage = new CustomErrorMessage(new Date(), ex.getMessage());
		return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.NOT_FOUND);
		// this class will handle any other unknown exception
	}
}
