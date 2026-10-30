package com.example.hello.broker;

/** Der Service wirft sie, der ApiExceptionHandler macht daraus 404. */
public class NotFoundException extends RuntimeException {

	public NotFoundException(String message) {
		super(message);
	}
}
