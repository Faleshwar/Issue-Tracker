package com.issueflow.exception;

public class IssueAlreadyExistException extends RuntimeException {
    public IssueAlreadyExistException(String message) {
        super(message);
    }
}
