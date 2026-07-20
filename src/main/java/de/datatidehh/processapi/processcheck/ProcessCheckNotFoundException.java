package de.datatidehh.processapi.processcheck;

public class ProcessCheckNotFoundException extends RuntimeException {

    public ProcessCheckNotFoundException(Long id) {
        super("Process check not found: " + id);
    }
}