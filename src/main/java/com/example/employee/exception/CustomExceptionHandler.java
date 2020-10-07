package com.example.employee.exception;

import com.example.employee.constants.EmployeeConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;


/**
 * Exception handler class for the Project
 */
@ControllerAdvice(annotations = RestController.class)
public class CustomExceptionHandler {
    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<String> unsupportedOperationException(UnsupportedOperationException e){
        String errors = EmployeeConstants.UNSUPPORTED_EXCEPTION_MESSAGE+ Arrays.toString(EmployeeConstants.ALLOWED_FILE_TYPES.toArray());
        return new ResponseEntity<String>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> validationErrorHandler(MethodArgumentNotValidException e){
        String errors = e.getLocalizedMessage();
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String>  handleMissingParams(MissingServletRequestParameterException ex) {
        String errors = EmployeeConstants.MISSING_REQUEST_MESSAGE+ex.getParameterName();
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
