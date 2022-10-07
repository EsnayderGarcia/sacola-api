package com.snayder.sacolaapi.resources.exceptions;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StandardError {
	
	private LocalDateTime timestamp;
	
	private int status;
	
	private String error;
	
	private String path;
	
}
