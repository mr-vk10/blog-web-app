package com.app.blog.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.app.blog.exceptions.IncorrectJwtException;


@ControllerAdvice
public class MyExceptionHandler<ProductAlreadyPresentException> {
    
    // add Exception handling code here
    @ExceptionHandler
    public ResponseEntity<Object> handleException(IncorrectJwtException exc){
        
        
        // return new ResponseEntity<>(exc,HttpStatus.OK);
    	return ResponseEntity.ok(exc.getMessage());
    }
    
    // add another exception handler ... to catch any exception (catch all)
    @ExceptionHandler
    public ResponseEntity<Object> handleException(Exception exc){
                
        // return ResponseEntity
        return new ResponseEntity<>(exc,HttpStatus.BAD_REQUEST);        
    }
}
