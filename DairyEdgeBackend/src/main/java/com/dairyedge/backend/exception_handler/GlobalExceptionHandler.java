package com.dairyedge.backend.exception_handler;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dairyedge.backend.custom_exception.BadRequestException;
import com.dairyedge.backend.custom_exception.InvalidInputException;
import com.dairyedge.backend.custom_exception.ResourceNotFoundException;
import com.dairyedge.backend.custom_exception.UnauthorizedException;
import com.dairyedge.backend.dtos.ApiResponse;

@RestControllerAdvice

public class GlobalExceptionHandler {
	
	@ExceptionHandler(Exception.class)
	// @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR )
	public ResponseEntity<?> handleException(Exception e) {
		System.out.println("in catch all ");
//			return ResponseEntity/* .status(HttpStatus.INTERNAL_SERVER_ERROR) */
//				 .of(Optional.of(new ApiResponse("Failed", e.getMessage())));
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Failed", e.getMessage()));
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException e) {
		System.out.println("in catch - ResourceNotFoundException");
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Failed", e.getMessage()));
	}
	
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<?> handleBadRequestException(BadRequestException e) {
		System.out.println("in catch -Spring sec detected  UsernameNotFoundException Exception "+e);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Failed", e.getMessage()));
	}

	@ExceptionHandler(InvalidInputException.class)
	public ResponseEntity<?> handleInvalidInputException(InvalidInputException e) {
		System.out.println("in catch -Spring sec detected  Authentication Exception "+e);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Failed", e.getMessage()));
	}

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<?> handleUnauthorizedException(UnauthorizedException e) {
		System.out.println("in catch -Spring sec detected  Authentication Exception "+e);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse("Failed", e.getMessage()));
	}

	/*
	 * To handle - MethodArgNotValid - Trigger - @Valid
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		System.out.println("in catch - MethodArgumentNotValidException");
		// 1. Get List of rejected fields
		List<FieldError> list = e.getFieldErrors();
		// 2. Convert list of FieldErrors -> Map<Key - field name , Value - err mesg>
		/*
		 * Map<String, String> map=new HashMap<>(); for(FieldError field : list)
		 * map.put(field.getField(), field.getDefaultMessage());
		 * 
		 * return ResponseEntity.status(HttpStatus.BAD_REQUEST) .body(map);
		 */

		Map<String, String> map = list.stream()
				.collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage,(v1,v2) -> v1+" "+v2));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
	}

}
