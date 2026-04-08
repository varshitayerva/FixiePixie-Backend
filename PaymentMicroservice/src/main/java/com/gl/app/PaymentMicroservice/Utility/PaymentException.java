package com.gl.app.PaymentMicroservice.Utility;

import org.springframework.http.HttpStatus;

public class PaymentException extends RuntimeException {
  private final HttpStatus status;

  public PaymentException(String message) {
    this(HttpStatus.BAD_REQUEST, message);
  }

  public PaymentException(String message, Throwable cause) {
    this(HttpStatus.BAD_REQUEST, message, cause);
  }

  public PaymentException(HttpStatus status, String message) {
    super(message);
    this.status = status;
  }

  public PaymentException(HttpStatus status, String message, Throwable cause) {
    super(message, cause);
    this.status = status;
  }

  public HttpStatus getStatus() {
    return status;
  }
}
