/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frankokafor.rest.exceptions;

/**
 *
 * @author Dexterio
 */
public class FileStorageException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FileStorageException(String message) {
        super(message);
    }

    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
