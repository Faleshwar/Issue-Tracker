package com.issueflow.exception;

import com.issueflow.response.ErrorResponse;
import com.issueflow.response.ValidationErrorResponse;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotfoundException(UsernameNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProjectException(ProjectNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(IssueNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleIssueException(IssueNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(ProjectMemberException.class)
    public ResponseEntity<ErrorResponse> handleProjectMemberException(ProjectMemberException ex){
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(ProjectAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handleProjectExistException(ProjectAlreadyExistException ex){
        return ResponseEntity.status(HttpStatus.FOUND).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(IssueAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handleIssueExistException(IssueAlreadyExistException ex){
        return ResponseEntity.status(HttpStatus.FOUND).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(InvalidRoleException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRoleException(InvalidRoleException ex){
        return ResponseEntity.status(HttpStatus.FOUND).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(ConflictException ex){
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationException(MethodArgumentNotValidException ex){
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error->error.getField()+" : "+error.getDefaultMessage())
                .toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ValidationErrorResponse("Validation Error", errors));     
    }

}
