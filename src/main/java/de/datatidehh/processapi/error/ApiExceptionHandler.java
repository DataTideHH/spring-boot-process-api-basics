package de.datatidehh.processapi.error;

import de.datatidehh.processapi.processcheck.ProcessCheckNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ProcessCheckNotFoundException.class)
    public ProblemDetail handleProcessCheckNotFound(
            ProcessCheckNotFoundException exception
    ) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                exception.getMessage()
        );

        problemDetail.setTitle("Process check not found");

        return problemDetail;
    }
}