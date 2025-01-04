package com.hnptech.musn.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

  private static class ErrorResponse {
    private final HttpStatus code;
    private final String message;

    public ErrorResponse(HttpStatus code, String message) {
      this.code = code;
      this.message = message;
    }

    public HttpStatus getCode() {
      return code;
    }

    public String getMessage() {
      return message;
    }
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<?> badRequestHandler(Exception ex) {
    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<?> unauthorizedHandler(Exception ex) {
    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
  }

  @ExceptionHandler(ForbiddenException.class)
  public ResponseEntity<?> forbiddenHandler(Exception ex) {
    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.FORBIDDEN, ex.getMessage());
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<?> notFoundHandler(Exception ex) {
    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> serverErrorHandler(Exception ex) {
    ex.printStackTrace();
    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL SERVER ERROR");
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }
}
