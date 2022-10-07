package com.snayder.sacolaapi.services.exceptions;

public class SacolaApiException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SacolaApiException(String message) {
		super(message);
	}
}
