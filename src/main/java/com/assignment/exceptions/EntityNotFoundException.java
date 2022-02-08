package com.assignment.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.logging.Level;
import java.util.logging.Logger;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RuntimeException {
    private static final Logger LOG = Logger.getLogger(EntityNotFoundException.class.getName());

    public EntityNotFoundException(String reason) {
        super(reason);
        LOG.log(Level.WARNING, reason, HttpStatus.NOT_FOUND);
    }
}
