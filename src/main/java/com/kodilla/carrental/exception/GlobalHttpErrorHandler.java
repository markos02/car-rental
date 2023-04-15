package com.kodilla.carrental.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalHttpErrorHandler {

    @ExceptionHandler(CarGroupNotFoundException.class)
    public ResponseEntity<Object> handleCarGroupNotFoundException(CarGroupNotFoundException exception) {
        return new ResponseEntity<>("Car group with given id doesn't exist", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DamageNotFoundException.class)
    public ResponseEntity<Object> handleDamageNotFoundException(DamageNotFoundException exception) {
        return new ResponseEntity<>("Damage with given id doesn't exist", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CarNotFoundException.class)
    public ResponseEntity<Object> handleCarNotFoundException(CarNotFoundException exception) {
        return new ResponseEntity<>("Car with given id doesn't exist", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RentalNotFoundException.class)
    public ResponseEntity<Object> handleRentalNotFoundException(RentalNotFoundException exception) {
        return new ResponseEntity<>("Rental with given id doesn't exist", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<Object> handleClientNotFoundException(ClientNotFoundException exception) {
        return new ResponseEntity<>("Client with given id doesn't exist", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Object> handleOrderNotFoundException(OrderNotFoundException exception) {
        return new ResponseEntity<>("Order with given id doesn't exist", HttpStatus.NOT_FOUND);
    }
}
