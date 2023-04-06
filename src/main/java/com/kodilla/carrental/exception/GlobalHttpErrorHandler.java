package com.kodilla.carrental.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalHttpErrorHandler {

    @ExceptionHandler(CarGroupNotFoundException.class)
    public ResponseEntity<Object> handleCarGroupNotFoundException (CarGroupNotFoundException  exception) {
        return new ResponseEntity<>("Car group with given id doesn't exist", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DamageNotFoundException.class)
    public ResponseEntity<Object> handleDamageNotFoundException (DamageNotFoundException  exception) {
        return new ResponseEntity<>("Damage with given id doesn't exist", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CarNotFoundException.class)
    public ResponseEntity<Object> handleCarNotFoundException (CarNotFoundException  exception) {
        return new ResponseEntity<>("Car with given id doesn't exist", HttpStatus.NOT_FOUND);
    }
}
