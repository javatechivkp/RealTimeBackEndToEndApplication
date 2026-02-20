package com.org.java.app.exceptions;

@SuppressWarnings("serial")
public class EmptyInputException extends RuntimeException {
	
	public EmptyInputException(String message) {
        super(message);
    }
	
}
