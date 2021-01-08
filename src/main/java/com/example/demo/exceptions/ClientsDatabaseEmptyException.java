package com.example.demo.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "The database is empty")
public class ClientsDatabaseEmptyException extends RuntimeException {
    public ClientsDatabaseEmptyException(String clients_database_is_empty) {
        super(clients_database_is_empty);
    }
}
