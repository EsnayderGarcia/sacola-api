package com.snayder.sacolaapi.resources.exceptions;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.snayder.sacolaapi.services.exceptions.ResourceNotFoundException;
import com.snayder.sacolaapi.services.exceptions.SacolaApiException;

@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException ex, 
											  HttpServletRequest req){
		 var error = StandardError.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.NOT_FOUND.value())
				.error(ex.getMessage())
				.path(req.getRequestURI())
				.build(); 
		
		return new ResponseEntity<StandardError>(error, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(SacolaApiException.class)
	public ResponseEntity<StandardError> SacolaApi(SacolaApiException ex, 
											  HttpServletRequest req){
		 var error = StandardError.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.BAD_REQUEST.value())
				.error(ex.getMessage())
				.path(req.getRequestURI())
				.build(); 
		
		return new ResponseEntity<StandardError>(error, HttpStatus.BAD_REQUEST);
	}
	
}





