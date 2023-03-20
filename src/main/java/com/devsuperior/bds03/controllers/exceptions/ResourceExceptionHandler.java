package com.devsuperior.bds03.controllers.exceptions;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ResourceExceptionHandler {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationError> validation(MethodArgumentNotValidException e, HttpServletRequest request){
		ValidationError error = new ValidationError();
		HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
		error.setTimestamp(Instant.now());
		error.setStatus(status.value());
		error.setError("Validation Exception!");
		error.setMessage(e.getMessage());
		
		//Para diminuir o string muito grande que está dando na resposta http, agora vai deixar a mensagem menos verboso
		//Criamos uma listinha de erros no validationError, para aparecer os erros resumidos facilitando para o front
		for(FieldError f : e.getBindingResult().getFieldErrors()) {
			error.addErrors(f.getField(), f.getDefaultMessage());
		}
		
		error.setPath(request.getRequestURI());
		return ResponseEntity.status(status).body(error);
		
	}
}
