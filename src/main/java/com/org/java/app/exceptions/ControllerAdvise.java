package com.org.java.app.exceptions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerAdvise  extends ResponseEntityExceptionHandler{
	
	 @Override
	    protected ResponseEntity<Object> handleMethodArgumentNotValid(
	            MethodArgumentNotValidException ex,
	            HttpHeaders headers,
	            HttpStatusCode status,
	            WebRequest request) {

	        Map<String, List<String>> errors = new HashMap<>();

	        ex.getBindingResult().getFieldErrors().forEach(error -> {
	            errors
	              .computeIfAbsent(error.getField(), k -> new ArrayList<>())
	              .add(error.getDefaultMessage());
	        });

	        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	    }
	
	
	
	@ExceptionHandler(EmptyInputException.class)
	public ResponseEntity<String> emptyInputExceptionHandller(EmptyInputException emptyInputException){
		return new ResponseEntity<String>("filed is empty please look it once",HttpStatus.BAD_REQUEST);
		
	}
	@ExceptionHandler(NoDataAvailableException.class)
	public ResponseEntity<String> noDataAvailableExceptionHandller(NoDataAvailableException noDataAvailableException){
		ErrorResponse empNotFound=new ErrorResponse(LocalDateTime.now(),noDataAvailableException.getMessage(),"Given id not prasent employee DB");
		return new ResponseEntity(empNotFound,HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(MethodNotAllowedException.class)
	public ResponseEntity<String> methodNotAllowedExceptionHandaller(MethodNotAllowedException  methodNotAllowedException ){
		return new ResponseEntity<String>("please the chage the verb name as soon as possiable..",HttpStatus.BAD_REQUEST);

	}
	
	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<String> nullPointerException(NullPointerException nullPointerException){
		return new ResponseEntity<String>("Given input no date availbale please check and change",HttpStatus.BAD_REQUEST);
		
	}
	 
	
	/*
	 * @ExceptionHandler(MethodArgumentNotValidException.class) public
	 * ResponseEntity<Map<String, String>> handleMultiErrors(
	 * MethodArgumentNotValidException ex) {
	 * 
	 * Map<String, String> errors = new HashMap<>();
	 * 
	 * ex.getBindingResult().getFieldErrors().forEach(error ->
	 * errors.put(error.getField(), error.getDefaultMessage()) );
	 * 
	 * return ResponseEntity.badRequest().body(errors); }
	 */
	}
	